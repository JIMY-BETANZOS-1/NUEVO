# -*- coding: utf-8 -*-
"""
Código de la sección 4 (Resultados) del artículo
"Factores que explican la intención de uso de asistentes de inteligencia artificial para programar en estudiantes de Ingeniería de Sistemas: una extensión del modelo de aceptación tecnológica".

Solo calcula lo que aparece en Resultados:
  4.1  Muestra, alfa de Cronbach, medias, DE y correlaciones (Tabla 2)
       Validez de constructo: análisis factorial exploratorio (Tabla 3)
  4.2  Regresión múltiple: modelo original y ampliado (Tabla 4) + comparación ΔR²
       Mediación: facilidad de uso -> utilidad percibida -> intención
  4.3  Supuestos: VIF, Shapiro-Wilk, Breusch-Pagan, Durbin-Watson, Cook
  4.4  Validación cruzada: LOOCV y 5 pliegues

Uso:
    python analisis_resultados.py [ruta_al_excel]

Por defecto lee data/Datos_encuesta_TAM_UPEU_anonimizado.xlsx (exportación de
Google Forms sin nombres), con las columnas en el orden del cuestionario (Anexo A).
"""

import sys

import numpy as np
import pandas as pd
from scipy import stats
import statsmodels.api as sm
from statsmodels.stats.outliers_influence import variance_inflation_factor
from statsmodels.stats.diagnostic import het_breuschpagan
from statsmodels.stats.stattools import durbin_watson
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import LeaveOneOut, KFold, cross_val_predict, cross_val_score
from sklearn.metrics import r2_score, mean_squared_error

# ---------------------------------------------------------------
# Datos: se renombran las columnas de Google Forms a códigos
# ---------------------------------------------------------------
RUTA = sys.argv[1] if len(sys.argv) > 1 else "data/Datos_encuesta_TAM_UPEU_anonimizado.xlsx"
crudo = pd.read_excel(RUTA)
crudo = crudo.iloc[:105]   # 105 respuestas recibidas hasta el 6 de noviembre, 19:37 (se excluyen las 2 posteriores)
items = ["UP1", "UP2", "UP3", "UP4", "FU1", "FU2", "FU3", "FU4",
         "IS1", "IS2", "IS3", "INT1", "INT2", "INT3"]
crudo.columns = ["FECHA", "NOMBRE", "CICLO", "EXP", "USO"] + items

likert = {"Totalmente en desacuerdo": 1, "En desacuerdo": 2, "Neutral": 3,
          "De acuerdo": 4, "Totalmente de acuerdo": 5}
anios = {"Menos de 1 año": 0, "1 año": 1, "2 años": 2, "3 años": 3,
         "4 años": 4, "5 años o más": 5}

print("Respuestas recibidas:", len(crudo))
df = crudo[crudo["USO"] == "Sí"].copy()          # solo quienes usaron asistentes de IA
for c in items:
    df[c] = df[c].map(likert)
df["EXP"] = df["EXP"].map(anios)
df = df.dropna(subset=items + ["EXP"])           # quita casos incompletos
n = len(df)
print("Casos válidos (n):", n)

escalas = {
    "INT": ["INT1", "INT2", "INT3"],
    "UP":  ["UP1", "UP2", "UP3", "UP4"],
    "FU":  ["FU1", "FU2", "FU3", "FU4"],
    "IS":  ["IS1", "IS2", "IS3"],
}

# ---------------------------------------------------------------
# 4.1 Muestra, confiabilidad y descriptivos (Tabla 2)
# ---------------------------------------------------------------
def alfa_cronbach(datos_items):
    k = datos_items.shape[1]
    return k / (k - 1) * (1 - datos_items.var(ddof=1).sum()
                          / datos_items.sum(axis=1).var(ddof=1))

print("\n=== 4.1 Tabla 2 ===")
print(df["CICLO"].value_counts())
print(crudo.loc[crudo["USO"] == "Sí", "EXP"].value_counts())

datos = pd.DataFrame()
for var, its in escalas.items():
    datos[var] = df[its].mean(axis=1)            # puntaje = promedio de los ítems
    print(f"{var}: alfa = {alfa_cronbach(df[its]):.3f}")
    sin_item = {it: round(float(alfa_cronbach(df[[x for x in its if x != it]])), 3) for it in its}
    print(f"   alfa si se elimina cada ítem: {sin_item}")
datos["EXP"] = df["EXP"].astype(float)

print(datos.agg(["mean", "std"]).T.round(2))     # medias y desviaciones estándar
print("\nCorrelaciones de Pearson:")
print(datos.corr().round(2))
for var in ["UP", "IS", "FU", "EXP"]:
    r, p = stats.pearsonr(datos["INT"], datos[var])
    print(f"  INT-{var}: r = {r:.2f}, p = {p:.3f}")

# ---------------------------------------------------------------
# 4.1 Validez de constructo: análisis factorial exploratorio (Tabla 3)
#     Ejes principales + rotación promax, programado con numpy
# ---------------------------------------------------------------
print("\n=== 4.1 Análisis factorial exploratorio ===")
R = df[items].astype(float).corr().values
p_it = len(items)

chi2 = -(n - 1 - (2 * p_it + 5) / 6) * np.log(np.linalg.det(R))
gl_b = p_it * (p_it - 1) / 2
print(f"Bartlett: chi2({gl_b:.0f}) = {chi2:.2f}, p = {stats.chi2.sf(chi2, gl_b):.4f}")

R_inv = np.linalg.inv(R)
parcial = -R_inv / np.sqrt(np.outer(np.diag(R_inv), np.diag(R_inv)))
np.fill_diagonal(parcial, 0)
R0 = R.copy()
np.fill_diagonal(R0, 0)
kmo = (R0**2).sum() / ((R0**2).sum() + (parcial**2).sum())
print(f"KMO = {kmo:.2f}")

autoval = np.sort(np.linalg.eigvalsh(R))[::-1]
print("Autovalores:", autoval.round(2), "| mayores a 1:", (autoval > 1).sum())
print(f"Harman (1.er factor): {autoval[0] / p_it * 100:.1f} % de la varianza")

rng = np.random.default_rng(42)                  # análisis paralelo (Horn, 1965)
azar = np.array([np.sort(np.linalg.eigvalsh(np.corrcoef(rng.normal(size=(n, p_it)),
                 rowvar=False)))[::-1] for _ in range(1000)])
p95 = np.percentile(azar, 95, axis=0)
print("Factores según análisis paralelo:", int(np.argmax(autoval < p95)))


def ejes_principales(R, k, iteraciones=1000):
    h2 = 1 - 1 / np.diag(np.linalg.inv(R))       # comunalidades iniciales
    Rr = R.copy()
    for _ in range(iteraciones):
        np.fill_diagonal(Rr, h2)
        val, vec = np.linalg.eigh(Rr)
        orden = np.argsort(val)[::-1][:k]
        L = vec[:, orden] * np.sqrt(np.clip(val[orden], 0, None))
        if np.max(np.abs((L**2).sum(1) - h2)) < 1e-6:
            break
        h2 = (L**2).sum(1)
    return L


def varimax(L, iteraciones=500):
    p, k = L.shape
    rot, d = np.eye(k), 0
    for _ in range(iteraciones):
        Lr = L @ rot
        u, s, vt = np.linalg.svd(L.T @ (Lr**3 - Lr @ np.diag((Lr**2).sum(0)) / p))
        rot = u @ vt
        if s.sum() - d < 1e-8 * s.sum():
            break
        d = s.sum()
    return L @ rot


def promax(L, potencia=4):
    h = np.sqrt((L**2).sum(1, keepdims=True))    # normalización de Kaiser
    V = varimax(L / h) * h
    U = np.linalg.lstsq(V, np.abs(V)**potencia * np.sign(V), rcond=None)[0]
    U = U @ np.diag(np.sqrt(np.diag(np.linalg.inv(U.T @ U))))
    # devuelve las cargas (matriz patrón) y la correlación entre factores
    return V @ U, np.linalg.inv(U.T @ U)


L = ejes_principales(R, 4)
cargas, phi = promax(L)
# se invierte el signo de un factor si su carga mayor es negativa
cargas = cargas * np.sign(cargas[np.abs(cargas).argmax(0), range(4)])
tabla3 = pd.DataFrame(cargas, index=items).round(2)
tabla3["h2"] = (L**2).sum(1).round(2)
print(tabla3)                                    # cada columna es un factor
print("Correlaciones entre factores:\n", np.abs(phi).round(2))

# Robustez: utilidad percibida sin UP1 (cargó más en facilidad de uso)
datos_sin_up1 = datos.assign(UP=df[["UP2", "UP3", "UP4"]].mean(axis=1))
m_sin = sm.OLS(datos_sin_up1["INT"],
               sm.add_constant(datos_sin_up1[["UP", "FU", "IS", "EXP"]])).fit()
print("Regresión con UP sin UP1 (p):", m_sin.pvalues.round(3).to_dict())

# ---------------------------------------------------------------
# 4.2 Regresión múltiple (Tabla 4)
# ---------------------------------------------------------------
y = datos["INT"]
X_orig = sm.add_constant(datos[["UP", "FU"]])
X_ampl = sm.add_constant(datos[["UP", "FU", "IS", "EXP"]])

m_orig = sm.OLS(y, X_orig).fit()
m_ampl = sm.OLS(y, X_ampl).fit()

print("\n=== 4.2 Regresión ===")
for nombre, m in [("Original", m_orig), ("Ampliado", m_ampl)]:
    k = m.df_model + 1                           # parámetros, con la constante
    aicc = m.aic + 2 * k * (k + 1) / (n - k - 1)
    print(f"\nModelo {nombre}: F({m.df_model:.0f}, {m.df_resid:.0f}) = {m.fvalue:.2f}, "
          f"p = {m.f_pvalue:.4f}, R² = {m.rsquared:.3f}, "
          f"R² aj. = {m.rsquared_adj:.3f}, AICc = {aicc:.1f}")
    print(pd.DataFrame({"b": m.params, "p": m.pvalues}).round(3))

# Beta estandarizado (β): mismo modelo con variables en puntajes z
z = (datos - datos.mean()) / datos.std()
m_z = sm.OLS(z["INT"], z[["UP", "FU", "IS", "EXP"]]).fit()
print("\nβ estandarizados (ampliado):")
print(m_z.params.round(3))

# β estandarizados del modelo original (UP y FU)
m_z0 = sm.OLS(z["INT"], z[["UP", "FU"]]).fit()
print("β estandarizados (original):")
print(m_z0.params.round(3))

# Errores estándar robustos HC3 (por si hay heterocedasticidad, ver 4.3)
m_rob = sm.OLS(y, X_ampl).fit(cov_type="HC3")
print("\nModelo ampliado con errores robustos HC3:")
print(pd.DataFrame({"b": m_rob.params, "EE": m_rob.bse, "p": m_rob.pvalues}).round(3))

# ¿La extensión mejora el modelo? Prueba F de ΔR² (modelos anidados)
F, p, gl = m_ampl.compare_f_test(m_orig)
d_r2 = m_ampl.rsquared - m_orig.rsquared
f2 = d_r2 / (1 - m_ampl.rsquared)
print(f"\nΔR² = {d_r2:.3f}, F({gl:.0f}, {m_ampl.df_resid:.0f}) = {F:.2f}, "
      f"p = {p:.3f}, f² = {f2:.2f}")

# Mediación: FU -> UP -> INT, con IS y EXP como covariables (Hayes, 2018)
def caminos(D):
    a = sm.OLS(D["UP"], sm.add_constant(D[["FU", "IS", "EXP"]])).fit().params["FU"]
    mod_b = sm.OLS(D["INT"], sm.add_constant(D[["UP", "FU", "IS", "EXP"]])).fit()
    return a, mod_b.params["UP"], mod_b.params["FU"]


m_a = sm.OLS(datos["UP"], sm.add_constant(datos[["FU", "IS", "EXP"]])).fit()
m_c = sm.OLS(datos["INT"], sm.add_constant(datos[["FU", "IS", "EXP"]])).fit()
a, b, c_dir = caminos(datos)
c_total = m_c.params["FU"]
print(f"\nMediación: a = {a:.3f} (p = {m_a.pvalues['FU']:.3f}), b = {b:.3f} "
      f"(p = {m_ampl.pvalues['UP']:.3f}), c = {c_total:.3f} (p = {m_c.pvalues['FU']:.3f}), "
      f"c' = {c_dir:.3f} (p = {m_ampl.pvalues['FU']:.3f})")

rng = np.random.default_rng(42)                  # bootstrap percentil, 5000 remuestreos
indirectos = []
for _ in range(5000):
    muestra = datos.iloc[rng.integers(0, n, n)].reset_index(drop=True)
    a_b, b_b, _ = caminos(muestra)
    indirectos.append(a_b * b_b)
li, ls = np.percentile(indirectos, [2.5, 97.5])
print(f"Efecto indirecto a·b = {a * b:.3f}, IC 95 % [{li:.3f}, {ls:.3f}], "
      f"proporción mediada = {a * b / c_total:.0%}")

# ---------------------------------------------------------------
# 4.3 Verificación de supuestos (modelo ampliado)
# ---------------------------------------------------------------
print("\n=== 4.3 Supuestos ===")
vif = [variance_inflation_factor(X_ampl.values, i) for i in range(1, X_ampl.shape[1])]
print("VIF:", [round(v, 2) for v in vif])

w, p_sw = stats.shapiro(m_ampl.resid)
print(f"Shapiro-Wilk: W = {w:.3f}, p = {p_sw:.3f}")

lm, p_bp, _, _ = het_breuschpagan(m_ampl.resid, X_ampl)
print(f"Breusch-Pagan: LM = {lm:.2f}, p = {p_bp:.3f}")

print(f"Durbin-Watson: {durbin_watson(m_ampl.resid):.2f}")

cook = m_ampl.get_influence().cooks_distance[0]
print(f"Distancia de Cook máxima: {cook.max():.3f}; "
      f"casos con D > 4/n ({4 / n:.3f}): {(cook > 4 / n).sum()}")

# Sensibilidad: regresión sin los casos con D de Cook > 4/n
sin_infl = (cook <= 4 / n)
m_sin_cook = sm.OLS(y[sin_infl], X_ampl[sin_infl]).fit()
print("Sin casos influyentes (p):", m_sin_cook.pvalues.round(3).to_dict())

# ---------------------------------------------------------------
# 4.4 Validación del modelo
# ---------------------------------------------------------------
print("\n=== 4.4 Validación cruzada ===")
for nombre, cols in [("Original", ["UP", "FU"]), ("Ampliado", ["UP", "FU", "IS", "EXP"])]:
    X = datos[cols]
    pred = cross_val_predict(LinearRegression(), X, y, cv=LeaveOneOut())
    rmse = mean_squared_error(y, pred) ** 0.5
    r2_5 = cross_val_score(LinearRegression(), X, y, scoring="r2",
                           cv=KFold(n_splits=5, shuffle=True, random_state=42)).mean()
    print(f"{nombre}: LOOCV R² = {r2_score(y, pred):.3f} (RMSE = {rmse:.3f}) | "
          f"5 pliegues R² medio = {r2_5:.3f}")

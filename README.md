# Intención de uso de asistentes de IA para programar (TAM) · UPeU

Código y datos del artículo empírico **"Factores que explican la intención de uso de asistentes de inteligencia artificial para programar en estudiantes de Ingeniería de Sistemas: una extensión del modelo de aceptación tecnológica"**.

Curso: Análisis Multivariado · Escuela de Ingeniería de Sistemas · Universidad Peruana Unión (UPeU), 2026.

**Autores:** Chatta Betanzos, Jimy Alexander · Cala Choquepata, Yheison Cristofer · Huarilloclla Huarilloclla, Josimar

## Qué hace el código

`analisis_resultados.py` reproduce todos los resultados del artículo (sección 4):

| Sección | Cálculo |
|---|---|
| 4.1 | Muestra, alfa de Cronbach, medias, desviaciones estándar y análisis factorial exploratorio (Tablas 2 y 3) |
| 4.2 | Regresión múltiple original y ampliada, ΔR² (Tabla 4), mediación con bootstrap (5000 remuestras) |
| 4.3 | Supuestos: VIF, Shapiro-Wilk, Breusch-Pagan (y errores HC3), Durbin-Watson, distancia de Cook |
| 4.4 | Validación cruzada: LOOCV y 5 pliegues |

## Datos

`data/Datos_encuesta_TAM_UPEU_anonimizado.xlsx` es la exportación de Google Forms con **107 respuestas**, sin la columna de nombres. El análisis usa las **primeras 105** (recibidas hasta el 6 de noviembre de 2026, 19:37); de ellas, **94** declararon haber usado asistentes de IA para programar y forman la muestra analizada.

Cuestionario: https://docs.google.com/forms/d/1ndfOmCXBJ3tZQOW0z2DHFjrKAUjpx4Qm_sJZ4LYZOFA/viewform

## Cómo ejecutarlo

```bash
python -m venv .venv
# Windows: .venv\Scripts\activate    |    Linux/macOS: source .venv/bin/activate
pip install -r requirements.txt
python analisis_resultados.py
```

Para usar otro archivo: `python analisis_resultados.py ruta/al/archivo.xlsx` (las columnas deben seguir el orden del cuestionario).

## Resultados principales (modelo ampliado, n = 94)

- R² = .465 (modelo original: .410; ΔR² = .055, p = .012)
- Utilidad percibida: β = .407, p < .001 · Influencia social: β = .271, p = .005
- Facilidad de uso y experiencia previa: no significativas como efectos directos
- Efecto indirecto de la facilidad de uso vía utilidad: 0.184, IC 95 % [0.059, 0.328]

## Nota ética

La participación fue voluntaria; el nombre era un dato opcional y se eliminó de la base compartida. Los resultados se presentan solo de forma agregada y con fines académicos.

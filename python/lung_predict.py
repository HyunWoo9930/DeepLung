import sys
import json
import numpy as np
import tensorflow as tf

def predict_from_input(model, input_dict):
    label_map = {0: "Low", 1: "Medium", 2: "High"}

    expected_cols = [
        'Age', 'Gender', 'Air Pollution', 'Alcohol use', 'Dust Allergy',
        'OccuPational Hazards', 'Genetic Risk', 'chronic Lung Disease',
        'Balanced Diet', 'Obesity', 'Smoking', 'Passive Smoker',
        'Chest Pain', 'Coughing of Blood', 'Fatigue', 'Weight Loss',
        'Shortness of Breath', 'Wheezing', 'Swallowing Difficulty',
        'Clubbing of Finger Nails', 'Frequent Cold', 'Dry Cough', 'Snoring'
    ]

    input_values = [input_dict[col] for col in expected_cols]
    input_array = np.array([input_values], dtype=np.float32)
    pred = model.predict(input_array)[0]
    return label_map[np.argmax(pred)]

try:
    user_input = json.loads(sys.stdin.read())
    import os
    model_path = os.path.join(os.path.dirname(__file__), "survey_only_lung_risk.h5")
    model = tf.keras.models.load_model(model_path)
    result = predict_from_input(model, user_input)
    print(result)
except Exception as e:
    print(f"[PY] ❌ 예외 발생: {e}", flush=True)

import tensorflow as tf
import numpy as np
import json
import sys
from tensorflow.keras.preprocessing import image

# 표준 입력으로부터 JSON 읽기
try:
    input_json = sys.stdin.read()
    input_data = json.loads(input_json)
except Exception as e:
    print(f"[PYTHON ERROR] 입력 JSON 파싱 실패: {e}")
    sys.exit(1)

# 컬럼 순서
survey_cols = [
    'Age', 'Gender', 'Air Pollution', 'Alcohol use', 'Dust Allergy',
    'OccuPational Hazards', 'Genetic Risk', 'chronic Lung Disease',
    'Balanced Diet', 'Obesity', 'Smoking', 'Passive Smoker',
    'Chest Pain', 'Coughing of Blood', 'Fatigue', 'Weight Loss',
    'Shortness of Breath', 'Wheezing', 'Swallowing Difficulty',
    'Clubbing of Finger Nails', 'Frequent Cold'
]

# === 문진 전처리 ===
try:
    survey_input = np.array([[input_data[col] for col in survey_cols]], dtype=np.float32)
except KeyError as e:
    print(f"[PYTHON ERROR] 누락된 필드: {e}")
    sys.exit(1)

# === X-ray 이미지 로딩 ===
try:
    img = image.load_img(input_data["xray_path"], target_size=(224, 224))
    img_array = image.img_to_array(img) / 255.0
    img_array = np.expand_dims(img_array, axis=0)
except Exception as e:
    print(f"[PYTHON ERROR] 이미지 로딩 실패: {e}")
    sys.exit(1)

# === 모델 로드 ===
try:
    import os
    model_path = os.path.join(os.path.dirname(__file__), "model_no_groups.h5")
    model = tf.keras.models.load_model(model_path)
except Exception as e:
    print(f"[PYTHON ERROR] 모델 로딩 실패: {e}")
    sys.exit(1)

# === 예측 ===
label_map = {0: "Low", 1: "Medium", 2: "High"}
try:
    pred = model.predict({"xray_input": img_array, "survey_input": survey_input})[0]
    result = label_map[np.argmax(pred)]
    print(result)
except Exception as e:
    print(f"[PYTHON ERROR] 예측 실패: {e}")
    sys.exit(1)

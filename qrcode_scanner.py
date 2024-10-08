from PIL import Image
from pyzbar.pyzbar import decode
import os
import cv2
import numpy as np
import pyboof as pb

class QR_Extractor:
    def __init__(self):
        self.detector = pb.FactoryFiducial(np.uint8).qrcode()
    def extract(self, img_path):
        image = pb.load_single_band(img_path, np.uint8)
        self.detector.detect(image)
        qr_codes = []
        for qr in self.detector.detections:
            qr_codes.append(qr.message)
        return qr_codes

detector = cv2.QRCodeDetector()
qr_scanner = QR_Extractor()
for filename in os.listdir():
    if not filename.endswith('.py'):
        links = {qr.data.decode('UTF-8') for qr in decode(Image.open(filename))}
        data, *_ = detector.detectAndDecode(cv2.imread(filename))
        if data: links.add(data)
        links |= {*qr_scanner.extract(filename)}
        print(filename, len(links))
        for link in links:
            print(link)
        print()
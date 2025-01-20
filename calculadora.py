import math

class CalculadoraCientifica:
    def __init__(self):
        pass
    
    def suma(self, a, b):
        return a + b
    
    def resta(self, a, b):
        return a - b
    
    def multiplicacion(self, a, b):
        return a * b
    
    def division(self, a, b):
        if b == 0:
            raise ValueError("No se puede dividir por cero")
        return a / b
    
    def potencia(self, base, exponente):
        return math.pow(base, exponente)
    
    def raiz_cuadrada(self, numero):
        if numero < 0:
            raise ValueError("No se pueden calcular raíces cuadradas de números negativos en los reales")
        return math.sqrt(numero)
    
    def seno(self, angulo):
        """Calcula el seno de un ángulo en radianes"""
        return math.sin(angulo)
    
    def coseno(self, angulo):
        """Calcula el coseno de un ángulo en radianes"""
        return math.cos(angulo)
    
    def tangente(self, angulo):
        """Calcula la tangente de un ángulo en radianes"""
        return math.tan(angulo)

# Ejemplo de uso
if __name__ == "__main__":
    calc = CalculadoraCientifica()
    print(f"Suma: {calc.suma(5, 3)}")
    print(f"Raíz cuadrada: {calc.raiz_cuadrada(16)}")
    print(f"Seno de 30 grados: {calc.seno(math.radians(30))}") 
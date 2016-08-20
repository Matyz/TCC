
import java.math.BigDecimal;
import java.math.RoundingMode;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Valderlei
 */
public class Matematica {

    public double distanciaEntre2Objetos(ObjectInfo objeto1, ObjectInfo objeto2) {
        double anguloRadiano = grausToRadiano(objeto2.m_direction);

        return Math.sqrt(Math.pow(objeto1.m_distance, 2) + Math.pow(objeto2.m_distance, 2)
                - 2 * objeto1.m_distance * objeto2.m_distance * Math.cos(anguloRadiano));
    }

    public double distanciaEntre2Pontos(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public double calcularAngulos(double a, double b, double c) {
        double angulo = (-1 * Math.pow(a, 2) + Math.pow(b, 2) + Math.pow(c, 2)) / (2 * b * c);
        return Math.acos(angulo);
    }

    public double getX(double distancia, double angulo) {
        double realAngulo = 90 - Math.abs(angulo);
        realAngulo = grausToRadiano(realAngulo);
        return Math.cos(realAngulo) * distancia;
    }

    public double getY(double distancia, double angulo) {
        double realAngulo = 90 - Math.abs(angulo);
        realAngulo = grausToRadiano(realAngulo);
        return Math.sin(realAngulo) * distancia;
    }

    //OS 2 primeiros valores indicam a igualdade , o 3 representa o valor a ser descoberto
    public double regraDeTres(double valor1, double valor2, double valor3) {
        double x = (valor3 * valor2) / valor1;
        BigDecimal bd = new BigDecimal(x).setScale(3, RoundingMode.HALF_EVEN);
        x = bd.doubleValue();
        return x;
    }

    public double grausToRadiano(double angulo) {
        return (Math.PI * Math.abs(angulo)) / 180;
    }

    public double radianoToGraus(double angulo) {
        return (angulo * 180) / Math.PI;
    }

}

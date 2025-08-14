package io.github.desafiodigital.models;

public class ConfiguracaoFase {

    String tipoFase;
    boolean bolderColor;
    String imagemMascote;

    int row, col;
    float marginX, marginY;
    float spacingX, spacingY;

    int qntNivel;

    public ConfiguracaoFase(
            String tipoFase, int qntNivel, String imagemMascote, 
            boolean bolderColor, int row, int col, 
            float marginX, float marginY, float spacingX, float spacingY) {

        this.row = row;
        this.col = col;
        this.marginX = marginX;
        this.marginY = marginY;
        this.tipoFase = tipoFase;
        this.qntNivel = qntNivel;
        this.spacingX = spacingX;
        this.spacingY = spacingY;
        this.bolderColor = bolderColor;
        this.imagemMascote = imagemMascote;
    }

    public boolean isBolderColor() {
        return bolderColor;
    }

    public String getTipoFase() {
        return tipoFase;
    }

    public String getImagemMascote() {
        return imagemMascote;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public float getMarginX() {
        return marginX;
    }

    public float getMarginY() {
        return marginY;
    }

    public float getSpacingX() {
        return spacingX;
    }

    public float getSpacingY() {
        return spacingY;
    }

    public int getQntNivel() {
        return qntNivel;
    }

    public void setQntNivel(int qntNivel) {
        this.qntNivel = qntNivel;
    }

    public void setTipoFase(String tipoFase) {
        this.tipoFase = tipoFase;
    }

    public void setBolderColor(boolean bolderColor) {
        this.bolderColor = bolderColor;
    }

    public void setImagemMascote(String imagemMascote) {
        this.imagemMascote = imagemMascote;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setMarginX(float marginX) {
        this.marginX = marginX;
    }

    public void setMarginY(float marginY) {
        this.marginY = marginY;
    }

    public void setSpacingX(float spacingX) {
        this.spacingX = spacingX;
    }

    public void setSpacingY(float spacingY) {
        this.spacingY = spacingY;
    }
}

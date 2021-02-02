package infile;

public class Result {
    public String getSno() {
        return sno;
    }
    public void setSno(String sno) {
        this.sno = sno;
    }
    public String getChinese() {
        return chinese;
    }
    public void setChinese(String chinese) {
        this.chinese = chinese;
    }
    public String getMath() {
        return math;
    }
    public void setMath(String math) {
        this.math = math;
    }
    public String getEnglish() {
        return english;
    }
    public void setEnglish(String english) {
        this.english = english;
    }
    public Result(String sno, String chinese, String math, String english) {
        this.sno = sno;
        this.chinese = chinese;
        this.math = math;
        this.english = english;
    }
    public Result() {
    }
    private String sno;
    private String chinese;
    private String math;
    private String english;
}

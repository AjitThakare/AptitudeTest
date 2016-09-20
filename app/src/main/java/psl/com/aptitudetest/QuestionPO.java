package psl.com.aptitudetest;

/**
 * Created by ajit_thakare on 7/12/2016.
 */
public class QuestionPO {
    String question;
    String opt1;
    String opt2;
    String opt3;
    String opt4;
    int correctAns;
    int questionID;

    public QuestionPO(int id,String question, String opt1, String opt2, String opt3, String opt4, int correctAns) {
        this.questionID=id;
        this.question = question;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt3 = opt3;
        this.opt4 = opt4;
        this.correctAns = correctAns;
    }

    public QuestionPO() {
        this.questionID=0;
        this.question = null;
        this.opt1 = null;
        this.opt2 = null;
        this.opt3 = null;
        this.opt4 = null;
        this.correctAns = 0;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOpt1() {
        return opt1;
    }

    public void setOpt1(String opt1) {
        this.opt1 = opt1;
    }

    public String getOpt2() {
        return opt2;
    }

    public void setOpt2(String opt2) {
        this.opt2 = opt2;
    }

    public String getOpt3() {
        return opt3;
    }

    public void setOpt3(String opt3) {
        this.opt3 = opt3;
    }

    public String getOpt4() {
        return opt4;
    }

    public void setOpt4(String opt4) {
        this.opt4 = opt4;
    }

    public int getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(int correctAns) {
        this.correctAns = correctAns;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    @Override
    public String toString() {
        return "QuestionPO{" +
                "Question ID="+questionID+'\''+
                "question='" + question + '\'' +
                ", opt1='" + opt1 + '\'' +
                ", opt2='" + opt2 + '\'' +
                ", opt3='" + opt3 + '\'' +
                ", opt4='" + opt4 + '\'' +
                ", correctAns=" + correctAns +
                '}';
    }
}

package ba.unsa.etf.nwt.user_service.response;

import ba.unsa.etf.nwt.user_service.model.Question;

public class QuestionResponse {
    private ResponseMessage responseMessage;
    private Question question;

    public QuestionResponse(ResponseMessage responseMessage, Question question) {
        this.responseMessage = responseMessage;
        this.question = question;
    }

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}

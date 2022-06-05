package org.polytech.zapros.service.answer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.polytech.zapros.bean.Answer;
import org.polytech.zapros.bean.Answer.AnswerAuthor;
import org.polytech.zapros.bean.Answer.AnswerType;
import org.polytech.zapros.bean.AnswerCheckResult;
import org.polytech.zapros.bean.Assessment;
import org.polytech.zapros.bean.BuildingQesCheckResult;
import org.polytech.zapros.bean.Criteria;
import org.polytech.zapros.bean.ReplacedAnswer;
import org.springframework.stereotype.Component;

@Component
public class AnswerServiceImpl implements AnswerService {

    @Override
    public AnswerCheckResult askFirst(List<Criteria> criteriaList) {
        AnswerCheckResult result = new AnswerCheckResult();
        result.setOver(false);
        result.setAnswerList(new ArrayList<>());
        result.setCriteriaList(criteriaList);
        result.setPCriteriaI(0);
        result.setPCriteriaJ(1);
        result.setPAssessmentI(1);
        result.setPAssessmentJ(1);
        return result;
    }

    @Override
    public AnswerCheckResult addAnswer(AnswerCheckResult checkResult, AnswerType answerType) {

        int pCriteriaI = checkResult.getPCriteriaI();
        int pCriteriaJ = checkResult.getPCriteriaJ();

        int pAssessmentI = checkResult.getPAssessmentI();
        int pAssessmentJ = checkResult.getPAssessmentJ();

        Criteria criteriaI = checkResult.getCriteriaList().get(pCriteriaI);
        Criteria criteriaJ = checkResult.getCriteriaList().get(pCriteriaJ);

        Assessment assessmentI = criteriaI.getAssessments().get(pAssessmentI);
        Assessment assessmentJ = criteriaJ.getAssessments().get(pAssessmentJ);

        List<Answer> answerList = new ArrayList<>(checkResult.getAnswerList());
        answerList.add(new Answer(assessmentI, assessmentJ, answerType, AnswerAuthor.USER));

        switch (answerType) {
            case BETTER: {
                for (int i = pAssessmentJ + 1; i < criteriaJ.getAssessments().size(); i++) {
                    Assessment tempAssessmentJ = criteriaJ.getAssessments().get(i);
                    answerList.add(new Answer(assessmentI, tempAssessmentJ, answerType, AnswerAuthor.TRANSIENT));
                }
                pAssessmentI++;
            } break;
            case WORSE: {
                for (int i = pAssessmentI + 1; i < criteriaI.getAssessments().size(); i++) {
                    Assessment tempAssessmentI = criteriaI.getAssessments().get(i);
                    answerList.add(new Answer(tempAssessmentI, assessmentJ, answerType, AnswerAuthor.TRANSIENT));
                }
                pAssessmentJ++;
            } break;
            case EQUAL: {
                for (int i = pAssessmentJ + 1; i < criteriaJ.getAssessments().size(); i++) {
                    Assessment tempAssessmentJ = criteriaJ.getAssessments().get(i);
                    answerList.add(new Answer(assessmentI, tempAssessmentJ, AnswerType.BETTER, AnswerAuthor.TRANSIENT));
                }
                for (int i = pAssessmentI + 1; i < criteriaI.getAssessments().size(); i++) {
                    Assessment tempAssessmentI = criteriaI.getAssessments().get(i);
                    answerList.add(new Answer(tempAssessmentI, assessmentJ, AnswerType.WORSE, AnswerAuthor.TRANSIENT));
                }
                pAssessmentI++;
                pAssessmentJ++;
            } break;
            default: throw new IllegalStateException("answerType TODO later");
        }

        // если мы сравнили ВСЕ оценки в рамках пары критериев
        if ((pAssessmentI == criteriaI.getAssessments().size()) ||
            (pAssessmentJ == criteriaJ.getAssessments().size())) {

            pCriteriaJ++;
            pAssessmentI = 1;
            pAssessmentJ = 1;
            // если второй критерий оказался крайним
            if (pCriteriaJ == checkResult.getCriteriaList().size()) {
                pCriteriaI++;
                pCriteriaJ = pCriteriaI + 1;

                if (pCriteriaI == checkResult.getCriteriaList().size() - 1) {
                    return new AnswerCheckResult(true, answerList);
                }
            }
        }

        AnswerCheckResult result = new AnswerCheckResult();
        checkResult.setOver(false);
        result.setAnswerList(answerList);
        result.setCriteriaList(checkResult.getCriteriaList());
        result.setPCriteriaI(pCriteriaI);
        result.setPCriteriaJ(pCriteriaJ);
        result.setPAssessmentI(pAssessmentI);
        result.setPAssessmentJ(pAssessmentJ);
        return result;
    }

    @Override
    public ReplacedAnswer replaceAnswer(BuildingQesCheckResult checkResult, AnswerType answerType) {
        List<Answer> answerList = checkResult.getAnswerList();
        Answer wrongAnswer = checkResult.getAnswerForReplacing();
        Answer newAnswer = new Answer(wrongAnswer.getI(), wrongAnswer.getJ(), answerType, AnswerAuthor.REPLACED);

        List<Answer> newAnswers = answerList.stream()
            .map(x -> x.equals(wrongAnswer) ? newAnswer : x)
            .collect(Collectors.toList());

        ReplacedAnswer result = new ReplacedAnswer();
        result.setReplacedAnswer(wrongAnswer);
        result.setNewAnswer(newAnswer);
        result.setNewAnswers(newAnswers);

        return result;
    }
}

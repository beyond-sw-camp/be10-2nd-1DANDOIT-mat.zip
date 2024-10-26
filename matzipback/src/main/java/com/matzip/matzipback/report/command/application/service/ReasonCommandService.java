package com.matzip.matzipback.report.command.application.service;

import com.matzip.matzipback.exception.ErrorCode;
import com.matzip.matzipback.exception.RestApiException;
import com.matzip.matzipback.report.command.application.dto.CreateReasonDTO;
import com.matzip.matzipback.report.command.application.dto.UpdateReasonNameDTO;
import com.matzip.matzipback.report.command.application.dto.UpdateReasonOrderDTO;
import com.matzip.matzipback.report.command.domain.aggregate.Reasons;
import com.matzip.matzipback.report.command.domain.repository.ReasonsRepository;
import com.matzip.matzipback.report.command.infrastructure.repository.ReasonInfraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReasonCommandService {

    private final ModelMapper modelMapper;
    private final ReasonsRepository reasonsRepository;
    private final ReasonInfraRepository reasonInfraRepository;

    // 신고사유 등록
    @Transactional
    public void createReason(CreateReasonDTO createReasonDTO) {
        log.info("========관리자의 신고사유 추가 서비스 진입========");

        // 현재 최댓값을 가져온 후 + 1 해줘서 제일 마지막으로 들어가게 설정해줌.
        Integer newOrder = reasonInfraRepository.findMaxReasonOrder() + 1;

        // DTO를 엔티티로 변환
        Reasons reasonEntity = modelMapper.map(createReasonDTO, Reasons.class);

        // 3. reason_order 값을 엔티티로 보내 기본값 재설정
        reasonEntity.updateReasonOrder(newOrder);

        // 4. 엔티티 저장
        reasonsRepository.save(reasonEntity);
    }

    // 신고사유명 수정
    @Transactional
    public void updateReasonName(Long reasonSeq, UpdateReasonNameDTO updateReasonNameDTO) {
        log.info("========관리자의 신고사유 수정 서비스 진입========");

        // 수정하고자 하는 seq가 없을 경우
        Reasons reason = reasonsRepository.findById(reasonSeq)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND));

        // 이름 업데이트
        log.info("사유명 수정 요청 : {}", updateReasonNameDTO);
        reason.updateReasonName(updateReasonNameDTO.getReasonName());
    }

    // 신고사유순서 변경
    @Transactional
    public void updateReasonOrder(Long reasonSeq, UpdateReasonOrderDTO updateReasonOrderDTO) {
        log.info("========관리자의 신고사유순서 변경 서비스 진입========");

        // 수정하고자 하는 seq가 없을 경우
        Reasons reason = reasonsRepository.findById(reasonSeq)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND));

        int currentOrder = reason.getReasonOrder();
        int newOrder = updateReasonOrderDTO.getReasonOrder();

        // 순서를 아래로 내리는 경우
        if (newOrder > currentOrder) {
            reasonInfraRepository.decrementOrder(currentOrder, newOrder);
        }
        // 순서를 위로 올리는 경우
        else if (newOrder < currentOrder) {
            reasonInfraRepository.incrementOrder(newOrder, currentOrder);
        }

        // 엔티티의 순서 업데이트
        log.info("사유순서 수정 요청 : {} -> {}", currentOrder, newOrder);
        reason.updateReasonOrder(newOrder);
    }

    // 신고사유 삭제
    @Transactional
    public void deleteReason(Long reasonSeq) {
        reasonsRepository.deleteById(reasonSeq);
    }
}

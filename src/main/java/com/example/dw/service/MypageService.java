package com.example.dw.service;


import com.example.dw.domain.entity.goods.Goods;
import com.example.dw.domain.entity.order.OrderReview;
import com.example.dw.domain.entity.user.Pet;
import com.example.dw.domain.entity.user.Users;
import com.example.dw.domain.form.*;
import com.example.dw.repository.community.WalkingMateRepository;
import com.example.dw.repository.community.WalkingMateStateRepository;
import com.example.dw.repository.order.OrderItemRepository;
import com.example.dw.repository.order.OrderReviewRepository;
import com.example.dw.repository.pet.PetRepository;
import com.example.dw.repository.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MypageService {

    private final FileService fileService;

    private final UsersRepository usersRepository;

    private final PetRepository petRepository;

    private final WalkingMateStateRepository walkingMateStateRepository;
    private final WalkingMateRepository walkingMateRepository;

    private final OrderReviewRepository orderReviewRepository;
    private final OrderItemRepository orderItemRepository;

    //회원정보 수정
    @Transactional
    public Users modify(UserUpdateForm userUpdateForm, MultipartFile file)
            throws IOException {
        System.out.println(userUpdateForm+"내용입니다");
        System.out.println(file+"적용되는 사진입니다");
        //수정된 메인 사진이 있다면 기존 사진 삭제 후 수정된 사진으로 업데이트
        System.out.println(!file.isEmpty());
        if(!file.isEmpty()) {
            System.out.println("사진 저장");
            //기존 사진 삭제
            fileService.removeUserImg(userUpdateForm.getId());

            //새로 수정된 사진 로컬 서버 저장 및 DB저장
            fileService.registerUserImg(file, userUpdateForm.getId());
        }else{
            System.out.println("입력된 메인 사진 없음");
        }




        Users users = usersRepository.findById(userUpdateForm.getId()).get();

        //상품 기본 내용 업데이트
        users.update(userUpdateForm);
        System.out.println(users+toString()+"수정입니다.");
        return Optional.ofNullable(users).orElseThrow(()->{
            throw new IllegalArgumentException("조회 정보 없음");
        });

    }

    //휴대폰 번호 중복체크
    @Transactional(readOnly = true)
    public boolean existsByUserPhone(String userPhone){
        return usersRepository.existsByUserPhone(userPhone);
    }


    //유저 닉네임 중복체크
    @Transactional(readOnly = true)
    public boolean existsByUserNickName(String userNickName){return  usersRepository.existsByUserNickName(userNickName);}

    /**
     * 등록된 펫 이름 중복확인
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    public boolean existsByPetName(String name, Long userId){

        return petRepository.existsByNameAndUsersId(name,userId);
    }

    @Transactional
    public Long register(PetForm petForm)throws IOException{
        System.out.println(petForm.toEntity()+"입니다.");

        Pet pet = petRepository.save(petForm.toEntity());

        System.out.println(pet.getUsers().getId()+"유저의 반려묘" + pet.getName()+"입니다.");

        return pet.getId();

    }

    /**
     * 펫 정보 삭제
     * @param petId
     */
    @Transactional
    public void removePet(Long petId){
        if(petId ==null){
            throw new IllegalArgumentException("펫 정보가 없습니다.");
        }
        petRepository.deleteById(petId);

    }

    @Transactional
    public Pet modifyPet(PetUpdateForm petUpdateForm, List<MultipartFile> files)
            throws IOException {
        System.out.println(petUpdateForm+"내용입니다");
        System.out.println(files+"적용되는 사진입니다");
        //수정된 메인 사진이 있다면 기존 사진 삭제 후 수정된 사진으로 업데이트
        System.out.println(!files.isEmpty());
        if(!files.isEmpty()) {
            System.out.println("사진 저장");
            //기존 사진 삭제
            fileService.removePetImgs(petUpdateForm.getId());

            //새로 수정된 사진 로컬 서버 저장 및 DB저장
            fileService.registerPetImg(files, petUpdateForm.getId());
        }else{
            System.out.println("입력된 메인 사진 없음");
        }


        Pet pet=petRepository.findById(petUpdateForm.getId()).get();

        //상품 기본 내용 업데이트
        pet.update(petUpdateForm);
        System.out.println(pet+toString()+"수정입니다.");
        return Optional.ofNullable(pet).orElseThrow(()->{
            throw new IllegalArgumentException("펫 정보 없음");
        });

    }

    // 산책글 신청인원 승인 처리
    @Transactional
    public Integer walkmatestateupdate(Long walkmatestateId){
        System.out.println(walkmatestateId);

        walkingMateStateRepository.upDateWalkMateState(walkmatestateId);

        Integer state = walkingMateStateRepository.findById(walkmatestateId).get().getState();
        System.out.println(state + "로 변경");
        System.out.println(walkmatestateId +"번 승인 완료");

        return state;
    }

    //산책글 신청인원 취소 처리

    @Transactional
    public Integer walkmatestatedown(Long walkmatestateId){
        System.out.println(walkmatestateId);

        walkingMateStateRepository.downWalkMateState(walkmatestateId);

        Integer state = walkingMateStateRepository.findById(walkmatestateId).get().getState();
        System.out.println(state + "로 변경");
        System.out.println(walkmatestateId +"번 승인 완료");

        return state;
    }

    //산책글 모집완료 상태로 변경
    @Transactional
    public Long walkingmatestateupdate(Long walkingmateId){

        System.out.println(walkingmateId);

        walkingMateRepository.updateWalkMateState(walkingmateId);

        Long walkingmatestate = walkingMateRepository.findById(walkingmateId).get().getWalkingMateState();

        System.out.println(walkingmatestate+"으로 값 변경");

        return  walkingmatestate;
    }


    @Transactional
    public Long registerreview(OrderReviewForm orderReviewForm)throws IOException{
        System.out.println(orderReviewForm.toEntity()+"입니다.");

        OrderReview orderReview = orderReviewRepository.save(orderReviewForm.toEntity());
        System.out.println(orderReview.getId()+"입니다.");
        System.out.println(orderReview.getOrderItem().getId()+"입니다.");
        orderItemRepository.updatrorderreview(orderReview.getOrderItem().getId());

        return orderReview.getId();

    }

    /**
     * 리뷰 삭제하기
     * @param id 삭제할 리뷰 아이디
     */
    @Transactional
    public void removeReview(Long id){
        if(id == null){
            throw new IllegalArgumentException("id 정보가 없습니다.");
        }
        fileService.removeReviewImgs(id);
        Long orderItemId =   orderReviewRepository.reviewId(id);
        System.out.println(orderItemId+" 삭제되어 스테이트가 변경되는 번호 입니다.");
        orderItemRepository.downorderreview(orderItemId);
        orderReviewRepository.deleteById(id);
    }

    //회원 탈퇴
    @Transactional
    public void removeUser(Long userId){

        if(userId == null){
            throw new IllegalArgumentException("회원정보가 없습니다.");
        }

      usersRepository.deleteUsersById(userId);
    }


}

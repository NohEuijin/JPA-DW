package com.example.dw.service;


import com.example.dw.domain.dto.admin.AdminGoodsDetailImgDto;
import com.example.dw.domain.dto.admin.AdminGoodsMainImgDto;
import com.example.dw.domain.dto.admin.PetImgDto;
import com.example.dw.domain.dto.admin.UserFileDto;
import com.example.dw.domain.dto.community.FreeBoardImgDto;
import com.example.dw.domain.dto.community.QuestionImgDto;
import com.example.dw.domain.entity.freeBoard.FreeBoard;
import com.example.dw.domain.dto.order.OrderReviewImgDto;
import com.example.dw.domain.entity.goods.Goods;
import com.example.dw.domain.entity.order.OrderReview;
import com.example.dw.domain.entity.question.Question;
import com.example.dw.domain.entity.user.Pet;
import com.example.dw.domain.entity.user.Users;
import com.example.dw.domain.form.*;
import com.example.dw.repository.community.QuestionImgRepository;
import com.example.dw.repository.community.QuestionRepository;
import com.example.dw.repository.community.QuestionRepositoryCustom;
import com.example.dw.repository.freeBoard.FreeBoardImgRepository;
import com.example.dw.repository.freeBoard.FreeBoardRepository;
import com.example.dw.repository.freeBoard.FreeBoardRepositoryCustom;
import com.example.dw.repository.goods.GoodsDetailImgRepository;
import com.example.dw.repository.goods.GoodsMainImgRepository;
import com.example.dw.repository.goods.GoodsRepository;
import com.example.dw.repository.order.OrderReviewImgRepository;
import com.example.dw.repository.order.OrderReviewImgRepositoryCustom;
import com.example.dw.repository.order.OrderReviewRepository;
import com.example.dw.repository.pet.PetImgRepository;
import com.example.dw.repository.pet.PetRepository;
import com.example.dw.repository.pet.PetRepositoryCustom;
import com.example.dw.repository.user.UserFileRepository;
import com.example.dw.repository.user.UsersRepository;
import com.example.dw.repository.user.UsersRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    @Value("${file.dir}")
    private String mainImg;

    @Value("${file.que}")
    private String questionImg;

    @Value("${file.free}")
    private String freeBoardImg;

    @Value("${file.user}")
    private String userFileImg;

    @Value("${file.pet}")
    private String petImg;

    @Value("${file.review}")
    private String reviewImg;

    private final GoodsMainImgRepository goodsMainImgRepository;
    private final GoodsDetailImgRepository goodsDetailImgRepository;
    private final GoodsRepository goodsRepository;

    private final QuestionRepository questionRepository;
    private final QuestionImgRepository questionImgRepository;
    private final QuestionRepositoryCustom questionRepositoryCustom;

    private final FreeBoardRepository freeBoardRepository;
    private final FreeBoardRepositoryCustom freeBoardRepositoryCustom;
    private final FreeBoardImgRepository freeBoardImgRepository;

    private final UsersRepository usersRepository;
    private final UserFileRepository userFileRepository;
    private final UsersRepositoryCustom usersRepositoryCustom;

    private final PetRepository petRepository;
    private final PetImgRepository petImgRepository;
    private final PetRepositoryCustom petRepositoryCustom;

    private final OrderReviewRepository orderReviewRepository;
    private final OrderReviewImgRepository orderReviewImgRepository;
    private final OrderReviewImgRepositoryCustom orderReviewImgRepositoryCustom;

    /**
     * 업로드 경로를 설정
     * @return 날짜를 기반, "yyyy/MM/dd" 형식 문자열
     */
    private String getUploadPath() {
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }

    //상품 메인 사진 로컬서버 저장
    @Transactional
    public GoodsMainImgForm saveImg(MultipartFile file) throws IOException {


        String originName = file.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String sysName = uuid.toString() + "_" + originName;

        File uploadPath = new File(mainImg, getUploadPath());

        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }
        File upLoadFile = new File(uploadPath, sysName);
        file.transferTo(upLoadFile);


        return
                GoodsMainImgForm.builder()
                        .goodsMainImgName(originName)
                        .goodsMainImgUuid(uuid.toString())
                        .goodsMainImgPath(getUploadPath())
                        .build();

    }

    //상품 메인 사진 DB 저장
    @Transactional
    public void registerMainImg(MultipartFile file, Long id) throws IOException {

        //컨트롤러를 통해 받아온 id 값(상품테이블 기본키)을 가지고
        //GoodsRepository에 만들어놓은 것을 활용
        //id값을 넣어 해당 id값과 일치하는 상품을 불러오고 이것을 넣어준다.

        GoodsMainImgForm goodsMainImgForm = saveImg(file);
        Optional<Goods> goods = goodsRepository.findById(id);


        goodsMainImgForm.setGoods(goods.get());
        goodsMainImgRepository.save(goodsMainImgForm.toEntity());


    }

    //상품 상세 사진 로컬서버 저장(최대 4장)
    @Transactional
    public GoodsDetailImgForm saveDetailImg(MultipartFile file) throws IOException {


        String originName = file.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String sysName = uuid.toString() + "_" + originName;

        File uploadPath = new File(mainImg, getUploadPath());

        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }
        File upLoadFile = new File(uploadPath, sysName);
        file.transferTo(upLoadFile);

        return
                GoodsDetailImgForm.builder()
                        .goodsDetailImgName(originName)
                        .goodsDetailImgUuid(uuid.toString())
                        .goodsDetailImgPath(getUploadPath())
                        .build();

    }

    //상품 상세 사진 DB 저장
    @Transactional
    public void registerDetailImg(List<MultipartFile> files, Long id) throws IOException {

        //파일 여러개
        for (MultipartFile file : files) {
            GoodsDetailImgForm goodsDetailImgForm = saveDetailImg(file);
            Optional<Goods> goods = goodsRepository.findById(id);

            goodsDetailImgForm.setGoods(goods.get());
            goodsDetailImgRepository.save(goodsDetailImgForm.toEntity());

        }

    }

    //메인 사진 삭제
    @Transactional
    public void removeMainImg(Long goodsId) {
        if (goodsId == null) {
            throw new IllegalArgumentException("유효하지 않은 상품 번호");
        }

        AdminGoodsMainImgDto img = goodsMainImgRepository.findByGoodsId(goodsId).get();

        AdminGoodsMainImgDto adminGoodsMainImgDto = new AdminGoodsMainImgDto(img.getId(), img.getGoodsMainImgPath(), img.getGoodsMainImgUuid(), img.getGoodsMainImgName(), img.getGoodsId());

        File mainImgTarget = new File(mainImg, adminGoodsMainImgDto.getGoodsMainImgPath() + "/" + adminGoodsMainImgDto.getGoodsMainImgUuid() + "_" + adminGoodsMainImgDto.getGoodsMainImgName());


        if (mainImgTarget.exists()) {
            mainImgTarget.delete();

            System.out.println("[ 삭제되는 상품 메인 사진 ]" + adminGoodsMainImgDto.getGoodsMainImgPath() + "/" + adminGoodsMainImgDto.getGoodsMainImgUuid() + "_" + adminGoodsMainImgDto.getGoodsMainImgName());
            goodsMainImgRepository.deleteById(adminGoodsMainImgDto.getId());

        }

    }

    //상세 사진 삭제
    @Transactional
    public void removeDetailImgs(Long goodsId) {

        if (goodsId == null) {
            throw new IllegalArgumentException("유효하지 않은 상품 번호");
        }

        List<AdminGoodsDetailImgDto> adminGoodsDetailImgDtos = goodsDetailImgRepository.findAllByGoodsId(goodsId);


        for (AdminGoodsDetailImgDto goodsDetailImg : adminGoodsDetailImgDtos) {
            File detailImgTarget = new File(mainImg, goodsDetailImg.getGoodsDetailImgPath() + "/" + goodsDetailImg.getGoodsDetailImgUuid() + "_" + goodsDetailImg.getGoodsDetailImgName());


            if (detailImgTarget.exists()) {
                detailImgTarget.delete();

                System.out.println("[ 삭제되는 상품 상세 사진 ]" + goodsDetailImg.getGoodsDetailImgPath() + "/" + goodsDetailImg.getGoodsDetailImgUuid() + "_" + goodsDetailImg.getGoodsDetailImgName() + "\n");
                goodsDetailImgRepository.deleteById(goodsDetailImg.getId());

            }
        }

    }

    /**
     * 자유게시판 파일처리
     */

    /**
     * 자유게시판 게시판 등록 파일 저장(로컬) 최대 5장
     */
    @Transactional
    public FreeBoardImgForm registerLocalFreeBoardImg(MultipartFile file) throws IOException {

        // 업로드된 파일의 원본 이름
        String originName = file.getOriginalFilename();
        //UUID를 사용하여 파일의 시스템 이름을 생성
        UUID uuid = UUID.randomUUID();
        String sysName = uuid.toString() + "_" + originName;

        //이미지를 업로드할 디렉토리 경로를 설정
        File uploadPath = new File(freeBoardImg, getUploadPath());
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }
        //업로드할 디렉토리가 존재하지 않으면 생성
        File upLoadFile = new File(uploadPath, sysName);
        //파일을 업로드 경로에 저장
        file.transferTo(upLoadFile);

        //업로드된 이미지 정보를 담은 FreeBoardImgForm 객체를 생성하여 반환
        return
                FreeBoardImgForm.builder()
                        .freeBoardImgName(originName)
                        .freeBoardImgRoute(getUploadPath())
                        .freeBoardImgUuid(uuid.toString())
                        .build();
    }

    /**
     * 자유게시판 이미지 DB 저장
     * @param files 여러 개의 이미지 파일을 담고 있는 리스트
     */
    @Transactional
    public void registerDBFreeBoardImg(List<MultipartFile> files, Long freeBoardId) throws IOException {

        for (MultipartFile file : files) {
            //이미지파일 로컬에 저장
            FreeBoardImgForm freeBoardImgForm = registerLocalFreeBoardImg(file);
            //게시물 찾기
            Optional<FreeBoard> freeBoard = freeBoardRepository.findById(freeBoardId);

            freeBoardImgForm.setFreeBoard(freeBoard.get());
            // 데이터베이스에 저장
            freeBoardImgRepository.save(freeBoardImgForm.toEntity());
        }
    }

    //자유게시판 기존 사진 삭제
    @Transactional
    public void removeFreeBoardDetailImgs(Long freeBoardId) {
        System.out.println("파일 삭제 번호 : " + freeBoardId);
        if (freeBoardId == null) {
            throw new IllegalArgumentException("유효하지 않은 자유게시판 번호");
        }
        System.out.println(freeBoardRepositoryCustom.findFreeBoardById(freeBoardId).toString() + "1-------1");
        List<FreeBoardImgDto> freeBoardImgDtos = freeBoardRepositoryCustom.findFreeBoardImgByFreeBoardId(freeBoardId);

        //가져온 이미지 목록을 반복, 각 이미지 파일을 삭제
        for (FreeBoardImgDto freeBoardImgDto : freeBoardImgDtos) {
            File detailImgTarget = new File(freeBoardImg, freeBoardImgDto.getFreeBoardImgRoute() + "/" + freeBoardImgDto.getFreeBoardImgUuid() + "_" + freeBoardImgDto.getFreeBoardImgName());
            System.out.println(freeBoardImgDto.toString() + "삭제 파일!");
            if (detailImgTarget.exists()) {
                detailImgTarget.delete();

                System.out.println("[ 삭제 사진 ]" + freeBoardImgDto.getFreeBoardImgRoute() + "/" + freeBoardImgDto.getFreeBoardImgUuid() + "_" + freeBoardImgDto.getFreeBoardImgName() + "\n");
                freeBoardImgRepository.deleteFreeBoardImgById(freeBoardImgDto.getId());
                System.out.println("자유게시판 삭제 완료");
            }
        }
    }

    //qna 게시판 등록 파일 저장(로컬) 최대 5장
    @Transactional
    public QuestionImgForm savequestionImg(MultipartFile file) throws IOException {


        String originName = file.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String sysName = uuid.toString() + "_" + originName;

        File uploadPath = new File(questionImg, getUploadPath());

        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }
        File upLoadFile = new File(uploadPath, sysName);
        file.transferTo(upLoadFile);

        return
                QuestionImgForm.builder()
                        .questionImgName(originName)
                        .questionImgRoute(getUploadPath())
                        .questionImgUuid(uuid.toString())
                        .build();


    }


    //que 이미지 DB 저장
    @Transactional
    public void registerquestionImg(List<MultipartFile> files, Long questionId) throws IOException {
        System.out.println("파일 처리 질문 아이기 : " + questionId);

        for (MultipartFile file : files) {
            QuestionImgForm questionImgForm = savequestionImg(file);
            Optional<Question> question = questionRepository.findById(questionId);

            questionImgForm.setQuestion(question.get());
            questionImgRepository.save(questionImgForm.toEntity());
        }

    }


    //que 기존 사진 삭제
    @Transactional
    public void removequeDetailImgs(Long questionId) {
        System.out.println("파일 삭제 아이디 : " + questionId);
        if (questionId == null) {
            throw new IllegalArgumentException("유효하지 않은 상품 번호");
        }
        System.out.println(questionRepositoryCustom.findAllByQuestionId(questionId).toString() + "1-------1");

        List<QuestionImgDto> questionImgDtos = questionRepositoryCustom.findAllByQuestionId(questionId);
        System.out.println(questionImgDtos.toString() + "나는 누구이인가");

        for (QuestionImgDto questionImgDto : questionImgDtos) {
            File detailImgTarget = new File(questionImg, questionImgDto.getQuestionImgRoute() + "/" + questionImgDto.getQuestionImgUuid() + "_" + questionImgDto.getQuestionImgName());
            System.out.println(questionImgDto.toString() + "삭제 파일 입니다.");
            if (detailImgTarget.exists()) {
                detailImgTarget.delete();

                System.out.println("[ 삭제 상품사진 ]" + questionImgDto.getQuestionImgRoute() + "/" + questionImgDto.getQuestionImgUuid() + "_" + questionImgDto.getQuestionImgName() + "\n");
                questionImgRepository.deleteQuestionImgById(questionImgDto.getId());
                System.out.println("완려");
            }

        }
    }

    /**
     * 유저 이미지 파일 처리
     */

    //user 이미지 로컬 저장
    @Transactional
    public UserFileForm saveUserFile(MultipartFile file) throws IOException {
        String originName = file.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String sysName = uuid.toString() + "_" + originName;

        File uploadPath = new File(userFileImg, getUploadPath());

        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }
        File upLoadFile = new File(uploadPath, sysName);
        file.transferTo(upLoadFile);

        return
                UserFileForm.builder()
                        .route(getUploadPath())
                        .name(originName)
                        .uuid(uuid.toString())
                        .build();
    }

    //user 이미지 DB 저장
    @Transactional
    public void registerUserImg(MultipartFile file, Long id) throws IOException {

        UserFileForm userFileForm = saveUserFile(file);
        Optional<Users> users = usersRepository.findById(id);

        userFileForm.setUsers(users.get());
        userFileRepository.save(userFileForm.toEntity());
    }

    //user 사진 삭제
    @Transactional
    public void removeUserImg(Long userId) {
        System.out.println("파일 삭제 아이디 : " + userId);
        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 유저 번호");
        }
        List<UserFileDto> userFileDtos = usersRepositoryCustom.findAllByUserId(userId);

        for (UserFileDto userFileDto : userFileDtos) {
            File detailImgTarget = new File(userFileImg, userFileDto.getRoute() + "/" + userFileDto.getUuid() + "_" + userFileDto.getName());
            if (detailImgTarget.exists()) {
                detailImgTarget.delete();
                userFileRepository.deleteById(userFileDto.getId());
            }
        }
    }

    //pet이미지 로컬 저장
    @Transactional
    public PetImgForm savePetImg(MultipartFile file) throws IOException {

        String originName = file.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String sysName = uuid.toString() + "_" + originName;

        File uploadPath = new File(petImg, getUploadPath());

        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }
        File upLoadFile = new File(uploadPath, sysName);
        file.transferTo(upLoadFile);

        return
                PetImgForm.builder()
                        .petFileName(originName)
                        .petPath(getUploadPath())
                        .petUuid(uuid.toString())
                        .build();


    }


    //pet 이미지 DB 저장
    @Transactional
    public void registerPetImg(List<MultipartFile> files, Long petId) throws IOException {
        System.out.println("파일 처리 질문 아이기 : " + petId);

        for (MultipartFile file : files) {
            PetImgForm petImgForm = savePetImg(file);
            System.out.println(petImgForm+"유관순");
            System.out.println(petImgForm.getPet()+"이게모야모야");
            Optional<Pet> pet = petRepository.findById(petId);

            petImgForm.setPet(pet.get());
            petImgRepository.save(petImgForm.toEntity());
        }

    }

    //pet 기존 사진 삭제
    @Transactional
    public void removePetImgs(Long petId) {
        System.out.println("파일 삭제 아이디 : " + petId);
        if (petId == null) {
            throw new IllegalArgumentException("유효하지 않은 펫 번호");
        }


        List<PetImgDto> petImgDtos = petRepositoryCustom.findAllPetById(petId);
        System.out.println(petImgDtos.toString() + "나는 누구이인가");

        for (PetImgDto petImgDto : petImgDtos) {
            File detailImgTarget = new File(petImg, petImgDto.getPetPath() + "/" + petImgDto.getPetUuid() + "_" + petImgDto.getPetFileName());
            System.out.println(petImgDto.toString() + "삭제 파일 입니다.");
            if (detailImgTarget.exists()) {
                detailImgTarget.delete();

                System.out.println("[ 삭제 펫사진 ]" + petImgDto.getPetPath() + "/" + petImgDto.getPetUuid() + "_" + petImgDto.getPetFileName()+ "\n");
                petImgRepository.deletePetImgById(petImgDto.getId());
                System.out.println("제발");
            }

        }
    }

     /**
     * @param file 저장할 파일
     * @return 저장된 리뷰 이미지에 대한 정보를 가진 리뷰 이미지 폼
     * @throws IOException 입출력시 예외 처리
     */
    @Transactional
    public OrderReviewImgForm savereviewImg(MultipartFile file) throws IOException {

        String originName = file.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String sysName = uuid.toString() + "_" + originName;

        File uploadPath = new File(reviewImg, getUploadPath());

        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }
        File upLoadFile = new File(uploadPath, sysName);
        file.transferTo(upLoadFile);

        return
                OrderReviewImgForm.builder()
                        .reviewimgFileName(originName)
                        .reviewimgPath(getUploadPath())
                        .reviewimgUuid(uuid.toString())
                        .build();
    }

    /**
     * review 이미지 DB 저장
     * @param files 업로드된 파일 목록
     * @param reviewId 리뷰 번호
     * @throws IOException 입출력시 예외 처리
     */
    @Transactional
    public void registerreviewImg(List<MultipartFile> files, Long reviewId) throws IOException {
        System.out.println("리뷰파일저장 번호 : " + reviewId);
        for (MultipartFile file : files) {
            OrderReviewImgForm orderReviewImgForm= savereviewImg(file);
            Optional<OrderReview> orderReview = orderReviewRepository.findById(reviewId);
            // 리뷰 이미지 폼에 주문 리뷰를 설정
            orderReviewImgForm.setOrderReview(orderReview.get());
            // 리뷰 이미지를 저장
            orderReviewImgRepository.save(orderReviewImgForm.toEntity());
        }
    }

    /**
     * 리뷰 삭제 하기기
     * @param reviewId 리뷰 번호
     */
    @Transactional
    public void removeReviewImgs(Long reviewId) {
        System.out.println("파일 삭제 아이디 : " + reviewId);
        if (reviewId == null) {
            throw new IllegalArgumentException("유효하지 않은 펫 번호");
        }
      List<OrderReviewImgDto> orderImgDtos = orderReviewImgRepositoryCustom.findReviewImgById(reviewId);
        for (OrderReviewImgDto orderImgDto : orderImgDtos) {
            File detailImgTarget = new File(reviewImg, orderImgDto.getReviewimgPath() + "/" + orderImgDto.getReviewimgUuid() + "_" + orderImgDto.getReviewimgFileName());
            System.out.println(orderImgDto.toString() + "삭제 파일 입니다.");
            if (detailImgTarget.exists()) {
                detailImgTarget.delete();
                orderReviewImgRepository.deleteById(orderImgDto.getId());
            }
        }
    }
}

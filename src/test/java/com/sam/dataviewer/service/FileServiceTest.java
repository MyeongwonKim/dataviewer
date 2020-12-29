package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.File;
import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.dto.FileDto;
import com.sam.dataviewer.repository.FileRepository;
import com.sam.dataviewer.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @InjectMocks
    private FileService fileService;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private Member member;
    @Captor
    private ArgumentCaptor<File> argumentCaptor;

    @Test
    @DisplayName("파일 업로드")
    public void uploadFileTest() throws Exception {
        //given
        MockMultipartFile file = getMultipartFile("sample.txt");

        //when
        String fileName = fileService.uploadFile(file);
        Path path = Path.of("C:/spring/dataviewer_files/" + fileName);

        //then
        then(fileName).contains(file.getOriginalFilename());
        then(Files.exists(path)).isEqualTo(true);

        //업로드된 파일 삭제
        Files.delete(path);
    }

    @Test
    @DisplayName("파일 업로드 및 정보 저장")
    public void saveFileTest() throws Exception {
        //given
        Order order = getOrder();
        MockMultipartFile file = getMultipartFile("sample.txt");
        List<MultipartFile> files = new ArrayList<>();
        files.add(file);
        Optional<Order> optional = Optional.of(order);
        given(orderRepository.findById(order.getId())).willReturn(optional);

        //when
        fileService.saveFile(order.getId(), files);

        //then
        verify(fileRepository, times(1)).save(argumentCaptor.capture());
        then(argumentCaptor.getValue()).isNotNull();
        then(argumentCaptor.getValue().getOriginalFileName()).isEqualTo("sample.txt");
        then(argumentCaptor.getValue().getFileName()).contains("sample.txt");
        then(argumentCaptor.getValue().getOrder()).isEqualTo(order);
        String filePath = argumentCaptor.getValue().getFilePath();
        then(Files.exists(Path.of(filePath))).isEqualTo(true);

        //업로드된 파일 삭제
        Files.delete(Path.of(filePath));
    }

    @Test
    @DisplayName("의뢰 id로 조회")
    public void findByOrderIdTest() throws Exception {
        //given
        Order order = getOrder();
        File file1 = getFile(order, "sample1.txt");
        File file2 = getFile(order, "sample2.txt");
        List<File> files = new ArrayList<>();
        files.add(file1);
        files.add(file2);
        Optional<Order> optional = Optional.of(order);
        given(orderRepository.findById(order.getId())).willReturn(optional);
        given(fileRepository.findByOrderOrderByIdDesc(order))
                .willReturn(files);

        //when
        List<FileDto> fileDtos = fileService.findByOrderId(order.getId());

        //then
        then(fileDtos.size()).isEqualTo(2);
        then(fileDtos.get(0).getOriginalFileName()).isEqualTo("sample1.txt");
        then(fileDtos.get(1).getOriginalFileName()).isEqualTo("sample2.txt");
    }

    @Test
    @DisplayName("파일 삭제")
    public void deleteFileTest() throws Exception {
        //given
        MockMultipartFile file = getMultipartFile("sample.txt");
        Path path = Path.of("C:/spring/dataviewer_files/" + file.getOriginalFilename());
        Files.write(path, file.getBytes());

        //when
        fileService.deleteFile(file.getOriginalFilename());

        //then
        then(false).isEqualTo(Files.exists(path));
    }

    @Test
    @DisplayName("파일 삭제 및 정보 삭제")
    public void deleteTest() throws Exception {
        //given
        Order order = getOrder();
        File fileDB = getFile(order, "sample.txt");
        MockMultipartFile file = getMultipartFile("sample.txt");
        Path path = Path.of("C:/spring/dataviewer_files/" + file.getOriginalFilename());
        Files.write(path, file.getBytes());
        Optional<File> optional = Optional.of(fileDB);
        given(fileRepository.findById(fileDB.getId())).willReturn(optional);

        //when
        fileService.delete(fileDB.getId());

        //then
        then(Files.exists(Path.of(fileDB.getFilePath()))).isEqualTo(false);
        verify(fileRepository).delete(fileDB);
    }

    @Test
    @DisplayName("파일 다운로드")
    public void downloadFileTest() throws Exception {
        //given
        MockMultipartFile file = getMultipartFile("sample.txt");
        Path path = Path.of("C:/spring/dataviewer_files/" + file.getOriginalFilename());
        Files.write(path, file.getBytes());

        //when
        Resource resource = fileService.downloadFile(file.getOriginalFilename());

        //then
        then(resource).isInstanceOf(InputStreamResource.class);

        //업로드된 파일 삭제
        Files.delete(path);
    }


    private File getFile(Order order, String originalFileName) {
        return File.createFile(
                order, originalFileName, null,
                "C:/spring/dataviewer_files/" + originalFileName, null
        );
    }

    private Order getOrder() {
        return Order.createOrder(
                member, "order", "content"
        );
    }

    private MockMultipartFile getMultipartFile(String originalFileName) {
        return new MockMultipartFile(
                "data", originalFileName,
                "text/plain", originalFileName.getBytes());
    }

}
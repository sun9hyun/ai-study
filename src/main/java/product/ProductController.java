package product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {
    // 상품 서비스 의존성 주입
    private final ProductService productService;

    /**
     * 상품 목록 조회 및 검색
     * @param id 상품 ID (선택)
     * @param name 상품명 (선택)
     * @param model 뷰에 데이터 전달
     * @return 상품 목록 화면
     */
    @GetMapping("/product")
    public String getProduct(@RequestParam(required = false) Long id,
                             @RequestParam(required = false) String name,
                             Model model) {
        model.addAttribute("id", id);
        model.addAttribute("name", name);

        // ID와 이름 모두 있을 때
        if (id != null && name != null && !name.isEmpty()) {
            List<ProductDTO> products = productService.getProductsByIdAndName(id, name);
            model.addAttribute("products", products);
        }
        // ID만 있을 때
        else if (id != null) {
            ProductDTO product = productService.getProductById(id);
            model.addAttribute("product", product);
        }
        // 이름만 있을 때
        else if (name != null && !name.isEmpty()) {
            List<ProductDTO> products = productService.getProductsByName(name);
            model.addAttribute("products", products);
        }
        // 검색 조건 없을 때 전체 조회
        else {
            List<ProductDTO> products = productService.getAllProducts();
            model.addAttribute("products", products);
        }
        return "productList";
    }

    /**
     * 상품 목록 엑셀 다운로드
     * @param response HttpServletResponse
     * @param id 상품 ID (선택)
     * @param name 상품명 (선택)
     * @throws IOException 엑셀 생성 오류
     */
    @GetMapping("/product/excel")
    public void downloadExcel(HttpServletResponse response,
                              @RequestParam(required = false) Long id,
                              @RequestParam(required = false) String name) throws IOException {
        List<ProductDTO> products;
        // 검색 조건에 따라 상품 목록 조회
        if (id != null && name != null && !name.isEmpty()) {
            products = productService.getProductsByIdAndName(id, name);
        } else if (id != null) {
            ProductDTO product = productService.getProductById(id);
            products = product != null ? List.of(product) : List.of();
        } else if (name != null && !name.isEmpty()) {
            products = productService.getProductsByName(name);
        } else {
            products = productService.getAllProducts();
        }

        // 엑셀 파일 응답 헤더 설정
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=products.xlsx");

        // 엑셀 파일 생성 및 데이터 입력
        try (var workbook = new XSSFWorkbook();
             var out = response.getOutputStream()) {
            var sheet = workbook.createSheet("상품목록");
            var header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("상품명");
            header.createCell(2).setCellValue("가격");

            // 상품 데이터 행 추가
            for (int i = 0; i < products.size(); i++) {
                var row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(products.get(i).getId());
                row.createCell(1).setCellValue(products.get(i).getName());
                row.createCell(2).setCellValue(products.get(i).getPrice());
            }
            workbook.write(out);
        }
    }
}
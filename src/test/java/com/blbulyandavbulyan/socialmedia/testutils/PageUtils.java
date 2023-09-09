package com.blbulyandavbulyan.socialmedia.testutils;

import com.blbulyandavbulyan.socialmedia.dtos.page.PageResponse;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

/**
 * Утилитный класс, предназначенный для облегчения написания тестов, где участвуют страницы
 */
public class PageUtils {

    private static final List<FieldDescriptor> basePageFieldsDescriptors = List.of(
            fieldWithPath("content").type(JsonFieldType.ARRAY).description("This array contains requested content"),
            fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("Total count of pages with this page size"),
            fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("Total count of elements"),
            fieldWithPath("pageSize").type(JsonFieldType.NUMBER).description("The given page size in the request"),
            fieldWithPath("number").type(JsonFieldType.NUMBER).description("Number of requested page(which you give in the request)"),
            fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("Indicates is this first page or not"),
            fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("Indicates is this last page or not")
    );

    /**
     * Принимает на вход мою Page DTO и возвращает mock Spring страницы
     *
     * @param dtoPageResponse моя Page DTO из которой будет создана spring Page
     * @param <T>     тип элемента в контенте страницы
     * @return созданный mock Spring page
     */
    public static <T> org.springframework.data.domain.Page<T> getMockPage(PageResponse<T> dtoPageResponse) {
        Page<T> result = Mockito.mock(Page.class);
        when(result.getTotalPages()).thenReturn(dtoPageResponse.totalPages());
        when(result.getSize()).thenReturn(dtoPageResponse.pageSize());
        when(result.getTotalElements()).thenReturn(dtoPageResponse.totalElements());
        when(result.getNumber()).thenReturn(dtoPageResponse.number() - 1);
        when(result.getContent()).thenReturn(dtoPageResponse.content());
        when(result.isFirst()).thenReturn(dtoPageResponse.first());
        when(result.isLast()).thenReturn(dtoPageResponse.last());
        return result;
    }

    /**
     * Генерирует ResponseFieldsSnippet для случая, если ответом является моя DTO Page
     *
     * @param fieldDescriptors дополнительное описания полей, содержащихся в content[]
     * @return сгенерированный ResponseFieldsSnippet, содержащий заданное описание полей и описание стандартных полей в моей DTO
     */
    public static ResponseFieldsSnippet generatePageResponseDescription(FieldDescriptor... fieldDescriptors) {
        ArrayList<FieldDescriptor> allFieldDescriptors = new ArrayList<>(basePageFieldsDescriptors);
        allFieldDescriptors.addAll(List.of(fieldDescriptors));
        return responseFields(allFieldDescriptors.toArray(FieldDescriptor[]::new));
    }
}

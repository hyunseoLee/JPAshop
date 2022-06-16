package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form", new bookForm());
        return "/items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(bookForm form){

        Book book= new Book();
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());

        itemService.saveItem(book);

        return "redirect:/items";

    }

     /* 조회하는 부분은 뭐가 잘못된거길래..!?!? 안될까?? */
    @GetMapping("/items")
    public String list(Model model){
        List<Item> items= itemService.findItems();
        model.addAttribute("items", items);
        return "/items/itemList";
    }



    /* 수정 하는 부분이 있음
    * */

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm (@PathVariable("itemId")  Long itemId, Model model){
        Book item =  (Book) itemService.findOne(itemId);
        bookForm form  = new bookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setAuthor(item.getAuthor());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setIsbn(item.getIsbn());


        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem (@ModelAttribute("form") bookForm form,@PathVariable("itemId")  Long itemId) {

        // 어설프게 컨트롤러에서 엔티티를 생성하지 마라.
       /* Book book = new Book();
        book.setId(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        */

        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";
    }

}


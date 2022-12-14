package com.softserve.edu.sporthubujp.controller;

import java.security.Principal;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.sporthubujp.dto.ArticleDTO;
import com.softserve.edu.sporthubujp.dto.ArticleListDTO;
import com.softserve.edu.sporthubujp.dto.ArticleSaveDTO;
import com.softserve.edu.sporthubujp.entity.Logs;
import com.softserve.edu.sporthubujp.repository.LogsRepository;
import com.softserve.edu.sporthubujp.entity.User;
import com.softserve.edu.sporthubujp.service.ArticleService;
import com.softserve.edu.sporthubujp.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class ArticleController {
    private final ArticleService articleService;
    private final UserService userService;
    private final LogsRepository logRepository;

    @Autowired
    public ArticleController(ArticleService articleService, UserService userService,
                             LogsRepository logRepository) {
        this.articleService = articleService;
        this.userService = userService;
        this.logRepository = logRepository;
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable String id) {
        log.info("Get article by id {}", id);
        CompletableFuture.supplyAsync(() -> logRepository.save(new Logs(id)));
        return ResponseEntity.status(HttpStatus.OK).body(
                articleService.getArticleById(id));
    }

    @GetMapping("/selected-articles")
    public ResponseEntity<List<ArticleListDTO>> getAllArticlesSelectedByAdmin() {
        log.info("Get all articles selected by admin");
        return ResponseEntity.status(HttpStatus.OK).body(
            articleService.getAllArticlesSelectedByAdmin());
    }

    @DeleteMapping("/admin/articles/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") String articleId) {
        log.info("Delete article by id {}", articleId);
        articleService.deleteArticleById(articleId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/articles/morePopular")
    public ResponseEntity<List<ArticleListDTO>> getMorePopularArticles(Pageable pageable) {
        log.info("Get more popular articles");
        return ResponseEntity.status(HttpStatus.OK).body(
            articleService.getMorePopularArticles(pageable));
    }

    @GetMapping("/articles/subscription")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<List<ArticleDTO>>
    getAllArticlesBySubscription(@NotNull Principal principal) {
        String email = principal.getName();
        log.info("Get all articles of the user with an email under {} subscription", email);
        User user = userService.findUserByEmail(email);
        log.info("Id user = {}", user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
            articleService.getAllArticlesBySubscription(user.getId()));
    }

    @GetMapping("/articles/category/{nameCategory}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<List<ArticleDTO>>
    getAllArticlesByCategoryName(@PathVariable String nameCategory) {
        log.info("Get all articles by category name: {}", nameCategory);
        return ResponseEntity.status(HttpStatus.OK).body(
            articleService.getAllArticlesByCategoryName(nameCategory));
    }

    @GetMapping("/articles/teams/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<List<ArticleListDTO>>
    getArticlesByTeamByUserId(@NotNull Principal principal, @PathVariable("id") String teamId) {
        String email = principal.getName();
        log.info("Get articles of the user with an email under {} subscription", email);
        User user = userService.findUserByEmail(email);
        log.info("Id user = {}", user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(
                articleService.getArticlesByTeamByUserId(user.getId(), teamId));
    }

    @PutMapping("/admin/articles")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ArticleDTO> selectedByAdmin(@RequestBody List<String> articleIDList) {
        log.info("Set in articles flag that  selectedByAdmin");
        articleService.selectArticleByAdmin(articleIDList);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping(path = "/articles/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ArticleDTO> updateArticle(@RequestBody @Valid ArticleSaveDTO newArticle,
                                                    @PathVariable("id") String id) {
        log.info("Update article by id {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(
                articleService.updateArticle(newArticle, id));
    }


    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/admin/articles")
    public ResponseEntity<Page<ArticleListDTO>> getAllArticles(Pageable pageable) {
        log.info("Get all articles");
        return ResponseEntity.status(HttpStatus.OK).body(
            articleService.getAllArticles(pageable));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/admin/articles/category_id/{id}")
    public ResponseEntity<Page<ArticleListDTO>>
    getAllArticlesByCategoryId(@PathVariable String id, Pageable pageable) {
        log.info("Get all articles by category id {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(
            articleService.getAllArticlesByCategoryId(id, pageable));
    }

    @GetMapping("/articles/category_id/{id}/is_active/{isActive}")
    public ResponseEntity<Page<ArticleListDTO>>
    getAllArticlesByCategoryIdAndIsActive(@PathVariable String id, @PathVariable boolean isActive, Pageable pageable) {
        log.info("Get all articles by category id {} and if article is active", id);
        return ResponseEntity.status(HttpStatus.OK).body(
            articleService.getAllArticlesByCategoryIdAndIsActive(id, isActive, pageable));
    }

    @GetMapping("/articles/{articleId}/categories/{categoryId}")
    public ResponseEntity<List<ArticleListDTO>>
    getSixActiveArticlesByCategoryId(@PathVariable String categoryId, @PathVariable String articleId) {
        log.info("Get 6 active articles by category id {}", categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(
            articleService.getSixActiveArticlesByCategoryId(categoryId, articleId));
    }

    @GetMapping("/articles/most_commented")
    public ResponseEntity<List<ArticleListDTO>> getMostCommentedArticles() {
        log.info("Get most commented articles");
        return ResponseEntity.status(HttpStatus.OK).body(
            articleService.getMostCommentedArticles());
    }


    @GetMapping("/articles/newest/{id}")
    public ResponseEntity<List<ArticleListDTO>>
    getFourNewestArticlesByCategoryId(@PathVariable("id") String categoryId, Pageable pageable) {
        log.info("Controller: getting four newest articles by category id");
        return ResponseEntity.status(HttpStatus.OK).body(
            articleService.getNewestArticlesByCategoryId(categoryId, pageable));
    }

    @GetMapping("/articles/team/{id}")
    public ResponseEntity<List<ArticleListDTO>>
    getAllArticlesByTeamId(@PathVariable("id") String id) {
        log.info("Controller: getting all articles by team id");
        return ResponseEntity.status(HttpStatus.OK).body(
                articleService.getAllArticlesByTeamId(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/admin/articles/publish/{id}")
    public ResponseEntity<ArticleDTO> publishUnpublishedArticle(@PathVariable("id") String id) {
        log.info("Publish or unpublished article by id {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(
            articleService.publishUnpublishedArticle(id));
    }

    @PostMapping(path = "/admin/articles")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ArticleSaveDTO> postArticle(@RequestBody @Valid ArticleSaveDTO newArticle) {
        log.info("Post article");
        return ResponseEntity.status(HttpStatus.OK).body(
            articleService.postArticle(newArticle));
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/admin/articles/category/{categoryId}/team/{teamId}")
    public ResponseEntity<Page<ArticleListDTO>>
    getAllArticlesCategoryIaAndTeamId(@PathVariable("categoryId") String categoryId,
                                      @PathVariable("teamId") String teamId, Pageable pageable) {
        log.info("Controller: getting all articles by team id");
        return ResponseEntity.status(HttpStatus.OK).body(
                articleService.getAllArticlesByCategoryIdAndTeamId(categoryId, teamId, pageable));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/admin/articles/category/{categoryId}/team/{teamId}/is_active/{isActive}")
    public ResponseEntity<Page<ArticleListDTO>>
    getAllArticlesCategoryIaAndTeamIdAndIsActive(@PathVariable("categoryId") String categoryId,
        @PathVariable("teamId") String teamId, @PathVariable("isActive") boolean isActive, Pageable pageable) {
        log.info("Controller: getting all articles by team id");
        return ResponseEntity.status(HttpStatus.OK).body(
                articleService.getAllArticlesByCategoryIdAndTeamIdAndIsActive(categoryId, teamId, isActive, pageable));
    }
}

package services;

import model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repositories.TagRepository;
import response.TagResponseBody;
import response.TagBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Component
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostService postService;

    @PersistenceContext
    EntityManager entityManager;

    public TagResponseBody getAllTags()
    {
        Iterable<Tag> tagIterable = tagRepository.findAll();
        List<Tag> tagsFromIterable = new ArrayList<>();
        for (Tag tag : tagIterable) {
            tagsFromIterable.add(tag);
        }
        ArrayList<TagBody> tags = getTagBodies(tagsFromIterable);
        TagResponseBody tagResponse = new TagResponseBody(tags);
        return tagResponse;
    }

    public TagResponseBody getSearchedTags(String query) {
        Query searchQuery = entityManager.createQuery("from Tag t where t.name like :tagName", Tag.class);
        searchQuery.setParameter("tagName", "%" + query + "%");
        List<Tag> currentTags = searchQuery.getResultList();
        ArrayList<TagBody> tags = getTagBodies(currentTags);
        TagResponseBody tagResponse = new TagResponseBody(tags);

        return tagResponse;
    }

    private ArrayList<TagBody> getTagBodies(List<Tag> currentTags) {
        ArrayList<TagBody> tags = new ArrayList<>();
        for (Tag tag : currentTags) {
            int postsCount = postService.getAllPostCount();
            double weight =  ((double) tag.getTagsPosts().size() / (double) postsCount) * 2;
            String tagName = tag.getName();
            TagBody tagResponseBody = TagBody.builder().weight(weight).name(tagName).build();
            tags.add(tagResponseBody);
        }
        return tags;
    }
}

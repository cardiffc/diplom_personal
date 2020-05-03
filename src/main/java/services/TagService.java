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

        /** TEST HQL YEAR **/

        /** select count(*) as posts_count from posts where moderation_status = 'ACCEPTED' and is_active = '1' and date_part('year', time) = '2020'; **/

        Query yearQuery = entityManager.createQuery("select year(time) as test1 from Post p where p.moderationStatus = 'ACCEPTED' group by test1");
        Query yearQuery1 = entityManager.createQuery("select to_char(time, 'YYYY-MM-DD') as test1, year(time) as test2, month(time) as mo from Post p where p.id = '1'");


        /** select count(*) as posts_count, date_part('year', time ) as year_count from posts group by year_count; **/

        Query tupleTest = entityManager.createQuery("select count(*) as posts_count, to_char(time,'YYYY') as year_count from Post p group by year_count");
        List<Object[]> tuple = tupleTest.getResultList();
        System.out.println(tuple.get(0)[0] + "/" + tuple.get(0)[1]);
        System.out.println(tuple.get(1)[0] + "/" + tuple.get(1)[1]);
        System.out.println("********************************************************");


        Query preProd = entityManager.createQuery("select count(*) from Post p where p.moderationStatus = 'ACCEPTED' and p.isActive = '1' and year(time) = '2020'");

       List<Long> prodInts = preProd.getResultList();

       prodInts.forEach(System.out::println);

       // System.out.println(prodInt);

        List<Integer> gg = yearQuery.getResultList();
        gg.forEach(System.out::println);


        List<Object[]> ll =  yearQuery1.getResultList();
        ll.forEach(element -> System.out.println(element[0] + "/" + element[1] + "/" + element[2]));

        //        Integer ll = yearQuery1.getFirstResult();
//
//        System.out.println(ll);


        /****/

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

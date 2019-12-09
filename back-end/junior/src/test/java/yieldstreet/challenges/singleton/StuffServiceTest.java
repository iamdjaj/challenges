package yieldstreet.challenges.singleton;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class StuffServiceTest {

    @Test
    public void testDefaultInstance() {
        List<StuffService> instances = IntStream.range(0, 100).parallel()
            .mapToObj(x -> StuffService.defaultInstance())
            .collect(Collectors.toList());

        StuffService first = instances.get(0);
        instances.forEach(instance -> assertThat(instance, sameInstance(first)));
    }

}

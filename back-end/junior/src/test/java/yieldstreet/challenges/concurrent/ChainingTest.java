package yieldstreet.challenges.concurrent;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ChainingTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule()
        .strictness(Strictness.STRICT_STUBS);

    @Mock
    private StuffRepository repository;

    @Before
    public void setUp() {
        // call the real method you should implement
        when(repository.findOrCreateStuff(any())).thenCallRealMethod();
    }

    @Test
    public void testFindOrCreateAbsent() {
        when(repository.findStuff(any()))
            .thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        when(repository.createStuff(any())).thenAnswer(invocation -> {
            String id = "new";
            return CompletableFuture.completedFuture(id);
        });

        Stuff stuff = new DefaultStuff(null, new Object());
        String result = repository.findOrCreateStuff(stuff).toCompletableFuture().join();

        assertThat(result, equalTo("new"));
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void testFindOrCreatePresent() {
        when(repository.findStuff(any())).thenReturn(
            CompletableFuture.completedFuture(Optional.of("existing")));

        Stuff stuff = new DefaultStuff(null, new Object());
        String result = repository.findOrCreateStuff(stuff).toCompletableFuture().join();

        assertThat(result, equalTo("existing"));
        verify(repository, never()).createStuff(any());
        verifyNoMoreInteractions(repository);
    }

}

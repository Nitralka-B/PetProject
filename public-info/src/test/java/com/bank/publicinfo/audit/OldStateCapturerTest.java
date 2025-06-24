package com.bank.publicinfo.audit;

import com.bank.publicinfo.entity.AuditableEntity;
import com.bank.publicinfo.testutil.TestConstants;
import com.bank.publicinfo.testutil.TestUtil;
import com.bank.publicinfo.util.StateSerializer;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Тестовый класс для {@link OldStateCapturer}.
 */
@ExtendWith(MockitoExtension.class)
class OldStateCapturerTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private StateSerializer stateSerializer;

    private OldStateCapturer oldStateCapturer;

    @BeforeEach
    void setUp() {
        oldStateCapturer = new OldStateCapturer(entityManager, stateSerializer);
    }

    /**
     * Проверяет корректность захвата состояния существующей сущности.
     */
    @Test
    void capture_ShouldStoreState_WhenEntityExists() {
        AuditableEntity testEntity = new TestUtil.TestEntity();
        Object expectedState = new Object();

        when(entityManager.find(any(Class.class), eq(TestConstants.TEST_ID))).thenReturn(testEntity);
        when(stateSerializer.deepCopy(testEntity)).thenReturn(expectedState);

        oldStateCapturer.capture(TestConstants.TEST_ID, testEntity.getClass());

        Object actualState = oldStateCapturer.getAndRemove(TestConstants.TEST_ID);
        assertEquals(expectedState, actualState);
        verify(entityManager).find(any(Class.class), eq(TestConstants.TEST_ID));
        verify(stateSerializer).deepCopy(testEntity);
    }

    /**
     * Проверяет обработку случая, когда сущность не найдена.
     */
    @Test
    void capture_ShouldNotStoreState_WhenEntityNotFound() {
        when(entityManager.find(any(Class.class), eq(TestConstants.TEST_ID))).thenReturn(null);

        oldStateCapturer.capture(TestConstants.TEST_ID, AuditableEntity.class);

        assertNull(oldStateCapturer.getAndRemove(TestConstants.TEST_ID));
        verify(entityManager).find(any(Class.class), eq(TestConstants.TEST_ID));
        verifyNoInteractions(stateSerializer);
    }

    /**
     * Проверяет корректность работы метода getAndRemove.
     */
    @Test
    void getAndRemove_ShouldReturnAndRemoveState() {
        AuditableEntity testEntity = new TestUtil.TestEntity();
        Object expectedState = new Object();

        when(entityManager.find(any(Class.class), eq(TestConstants.TEST_ID))).thenReturn(testEntity);
        when(stateSerializer.deepCopy(testEntity)).thenReturn(expectedState);
        oldStateCapturer.capture(TestConstants.TEST_ID, testEntity.getClass());

        Object firstRetrieval = oldStateCapturer.getAndRemove(TestConstants.TEST_ID);
        Object secondRetrieval = oldStateCapturer.getAndRemove(TestConstants.TEST_ID);

        assertEquals(expectedState, firstRetrieval);
        assertNull(secondRetrieval);
    }

    /**
     * Проверяет корректность очистки всех сохраненных состояний.
     */
    @Test
    void clear_ShouldRemoveAllStates() {
        Long entityId2 = TestConstants.TEST_ID_2;
        AuditableEntity testEntity = new TestUtil.TestEntity();
        Object state1 = new Object();
        Object state2 = new Object();

        when(entityManager.find(any(Class.class), eq(TestConstants.TEST_ID))).thenReturn(testEntity);
        when(entityManager.find(any(Class.class), eq(entityId2))).thenReturn(testEntity);
        when(stateSerializer.deepCopy(testEntity))
                .thenReturn(state1)
                .thenReturn(state2);

        oldStateCapturer.capture(TestConstants.TEST_ID, testEntity.getClass());
        oldStateCapturer.capture(entityId2, testEntity.getClass());

        oldStateCapturer.clear();

        assertNull(oldStateCapturer.getAndRemove(TestConstants.TEST_ID));
        assertNull(oldStateCapturer.getAndRemove(entityId2));
    }
}

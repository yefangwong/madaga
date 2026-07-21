package net.yefangwong.csp.common.entity;

import junit.framework.TestCase;
import java.util.Date;

public class BaseEntityTest extends TestCase {

    static class SampleEntity extends BaseEntity<Long> {
        private String name;

        public SampleEntity() {
            super();
        }

        public SampleEntity(Long id, String name) {
            super(id);
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public void testBaseEntityCreationAndHooks() throws InterruptedException {
        SampleEntity entity = new SampleEntity(100L, "TestEntity");

        assertEquals(Long.valueOf(100L), entity.getId());
        assertEquals("TestEntity", entity.getName());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());

        Date originalUpdate = entity.getUpdatedAt();
        Thread.sleep(10); // 短暫停頓確保時間有差異

        entity.preUpdate();
        assertTrue(entity.getUpdatedAt().getTime() >= originalUpdate.getTime());
    }

    public void testEqualsAndHashCode() {
        SampleEntity e1 = new SampleEntity(100L, "EntityA");
        SampleEntity e2 = new SampleEntity(100L, "EntityB");
        SampleEntity e3 = new SampleEntity(200L, "EntityA");

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
        assertFalse(e1.equals(e3));
    }
}

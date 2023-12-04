package org.example.category.domains;

import lombok.*;
import org.example.album.domains.IAlbum;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category implements ICategory{
        private long id;
        private String name;

        @Override
        public long getid() {
            return 0;
        }
        @Override
        public String getName() {

            return null;
        }
}

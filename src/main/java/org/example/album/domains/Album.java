package org.example.album.domains;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Album implements IAlbum {
    private long id;
    private String title;

    @Override
    public long getid() {
        return 0;
    }
    @Override
    public String gettitle() {
        return null;
    }
}

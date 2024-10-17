package com.matzip.matzipback.board.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "board_category")
@Getter
@NoArgsConstructor
public class BoardCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardCategorySeq;
    private String boardCategoryName;

}

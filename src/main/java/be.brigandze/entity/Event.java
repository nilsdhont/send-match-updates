package be.brigandze.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.bind.annotation.JsonbDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static be.brigandze.util.Utils.isNotNullString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    private int id;

    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss[XXX][X]")
    private LocalDateTime start_at;

    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss[XXX][X]")
    private LocalDateTime end_at;

    private Opponent opponent_left;
    private Opponent opponent_right;

    private Links _links;


    public void setEnd_at(Object end_at) {
        if (end_at != null && isNotNullString(end_at)) {
            this.end_at = LocalDateTime.parse((String) end_at,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[XXX][X]"));
        }
    }

    public void setEnd_atFromLocalDateTime(LocalDateTime end_at){
        this.end_at = end_at;
    }

}

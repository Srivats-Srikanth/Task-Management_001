package com.api.task.entity.custom;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomTaskCount {
//    Long getTotalCount();
//    Long getCompletedCount();
//    Long getPercentageCount();

    Long totalCount;
    Long completedCount;
    Double percentageCount;
}

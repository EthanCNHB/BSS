package com.wang.bss.mapper;

import com.wang.bss.pojo.College;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CollegeMapper {

    @Select("SELECT * FROM college")
    List<College> getAllColleges();

    @Select("SELECT * FROM college WHERE college_id = #{collegeId}")
    College getCollegeById(Integer collegeId);

    @Insert("INSERT INTO college (college_name, creation_time, college_description) " +
            "VALUES (#{collegeName}, #{creationTime}, #{collegeDescription})")
    void addCollege(College college);

    @Update("UPDATE college SET college_name = #{collegeName}, creation_time = #{creationTime}, " +
            "college_description = #{collegeDescription} WHERE college_id = #{collegeId}")
    void updateCollege(College college);

    @Delete("DELETE FROM college WHERE college_id = #{collegeId}")
    void deleteCollege(Integer collegeId);
}

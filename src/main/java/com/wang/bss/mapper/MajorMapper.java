package com.wang.bss.mapper;

import com.wang.bss.pojo.Major;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MajorMapper {

    @Select("SELECT * FROM major")
    List<Major> getAllMajors();

    @Select("SELECT * FROM major WHERE major_id = #{majorId}")
    Major getMajorById(Integer majorId);

    @Insert("INSERT INTO major (major_name, college_id, major_description) " +
            "VALUES (#{majorName}, #{collegeId}, #{majorDescription})")
    void addMajor(Major major);

    @Update("UPDATE major SET major_name = #{majorName}, college_id = #{collegeId}, " +
            "major_description = #{majorDescription} WHERE major_id = #{majorId}")
    void updateMajor(Major major);

    @Delete("DELETE FROM major WHERE major_id = #{majorId}")
    void deleteMajor(Integer majorId);

    @Select("SELECT * FROM major WHERE college_id = #{collegeId}")
    List<Major> findByCollegeId(Integer collegeId);
}

package com.pluralsight.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.pluralsight.model.Ride;
import com.pluralsight.repository.util.RideRowMapper;

@Repository("rideRepository")
public class RideRepositoryImpl implements RideRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

    @Override
    public Ride createRide(Ride ride){
//       jdbcTemplate.update("insert into ride (name, duration) values (?,?)", ride.getName(), ride.getDuration());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement("insert into ride (name, duration) values (?,?)", new String[]{"id"});
                ps.setString(1, ride.getName());
                ps.setInt(2, ride.getDuration());
                return ps;
            }
        }, keyHolder);

        Number id = keyHolder.getKey();
       return getRide(id.intValue());
    }

    @Override
    public Ride getRide(Integer id){
        return jdbcTemplate.queryForObject("select * from ride where id = ?", new RideRowMapper(), id);
    }

	@Override
	public List<Ride> getRides() {
        return jdbcTemplate.query("select * from ride", new RideRowMapper());
	}

    @Override
    public Ride updateRide(Ride ride) {
        jdbcTemplate.update("update ride set name = ?, duration = ? where id = ?", ride.getName(), ride.getDuration(), ride.getId());

        return getRide(ride.getId());
    }

    @Override
    public void updateRides(List<Object[]> pairs) {
        jdbcTemplate.batchUpdate("update ride set ride_date = ? where id = ?", pairs);
    }

    @Override
    public void deleteRide(Integer id) {
        jdbcTemplate.update("delete from ride where id = ?", id);
    }

}

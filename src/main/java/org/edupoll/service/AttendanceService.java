package org.edupoll.service;

import java.util.Optional;

import org.edupoll.model.entity.Attendance;
import org.edupoll.model.entity.Moim;
import org.edupoll.model.entity.User;
import org.edupoll.repository.AttendanceRepository;
import org.edupoll.repository.MoimRepository;
import org.edupoll.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class AttendanceService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	AttendanceRepository attendanceRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MoimRepository moimRepository;

	// 참가 신청 요청시 처리할 서비스 메서드
	@Transactional
	public boolean addNewAttendance(String userId, String moimId) {
		logger.debug("userId={}, moimId={}", userId, moimId);
		Optional<User> user = userRepository.findById(userId);
		Optional<Moim> moim = moimRepository.findById(moimId);
		if (user.isEmpty() || moim.isEmpty()) {
			return false;
		}
		if (attendanceRepository.existsByUserIdIsAndMoimIdIs(userId, moimId)) {
			return false;
		}
		if (moim.get().getCurrentPerson() == moim.get().getMaxPerson()) {
			return false;
		}
		Attendance one = new Attendance(user.get(), moim.get());
		attendanceRepository.save(one);

		moim.get().setCurrentPerson(moim.get().getCurrentPerson() + 1);
		moimRepository.save(moim.get());

		return true;
	}

}

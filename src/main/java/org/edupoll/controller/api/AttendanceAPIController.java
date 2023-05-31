package org.edupoll.controller.api;

import org.edupoll.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/api")
public class AttendanceAPIController {

	@Autowired
	AttendanceService attendanceService;

	@PostMapping("/attendance/join")
	@ResponseBody
	public String attendanceJoinHandle(@SessionAttribute String logonId, String moimId) {
		boolean rst = attendanceService.addNewAttendance(logonId, moimId);
		return String.valueOf(rst);
	}
}

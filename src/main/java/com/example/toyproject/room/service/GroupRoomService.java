package com.example.toyproject.room.service;

import com.example.toyproject.room.dto.RoomDto;
import com.example.toyproject.room.entity.GroupRoom;
import com.example.toyproject.room.entity.Room;
import com.example.toyproject.room.enums.RoomStatus;
import com.example.toyproject.room.enums.RoomType;
import com.example.toyproject.room.repository.GroupRoomRepository;
import com.example.toyproject.room.repository.RoomRepository;
import com.example.toyproject.room.repository.SingleRoomRepository;
import com.example.toyproject.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GroupRoomService {

    private SingleRoomRepository singleRoomRepository;
    private GroupRoomRepository groupRoomRepository;
    private RoomRepository roomRepository;

    public GroupRoomService(SingleRoomRepository singleRoomRepository, GroupRoomRepository groupRoomRepository, RoomRepository roomRepository) {
        this.singleRoomRepository = singleRoomRepository;
        this.groupRoomRepository = groupRoomRepository;
        this.roomRepository = roomRepository;
    }

    public RoomDto.SaveGroupRoomResponse saveGroupRoom(RoomDto.SaveGroupRoomRequest saveGroupRoomRequest, User user) {
        // 최대 인원수가 0 이하인 경우 예외 처리
        if (saveGroupRoomRequest.maxPlayers() <= 0) {
            throw new IllegalArgumentException("최대 인원수는 1명 이상이어야 합니다.");
        }
        Room room = new Room(saveGroupRoomRequest.password().isBlank(), saveGroupRoomRequest.roomName(), RoomType.GROUP, user, RoomStatus.OPEN, saveGroupRoomRequest.maxPlayers());
        roomRepository.save(room);
        String inviteCode = generateEncryptedInviteCode(user);
        GroupRoom groupRoom = new GroupRoom(inviteCode, room, saveGroupRoomRequest.password());
        groupRoomRepository.save(groupRoom);
        return new RoomDto.SaveGroupRoomResponse(room.getId(), room.getName(), room.getType().name(), groupRoom.getPassword(), groupRoom.getInvite_code());
    }

    public RoomDto.GroupRoomDto findGroupRoomByRoomId(Long roomId) {
        RoomDto.GroupRoomDto groupRoomDto = groupRoomRepository.findGroupRoomByRoomId(roomId);
        if (groupRoomDto == null) {
            throw new IllegalArgumentException("해당 ID의 그룹룸이 존재하지 않습니다: " + roomId);
        }
        return groupRoomDto;
    }



    // 사용자 ID와 시간을 암호화하여 고유 초대 코드 생성
    private String generateEncryptedInviteCode(User user) {
        // 사용자 ID를 기반으로 한 시드값 생성
        long userId = user.getId();
        long timestamp = System.currentTimeMillis();

        // ID를 암호화하는 간단한 방법 (XOR 연산과 비트 시프트 활용)
        long encryptedId = (userId ^ (timestamp >> 16)) & 0xFFFF;

        // 암호화된 ID를 base36 형식으로 변환 (더 짧고 알파벳+숫자 조합)
        String idPart = Long.toString(encryptedId, 36).toUpperCase();

        // 랜덤 문자 5자리 생성
        StringBuilder randomPart = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            // 랜덤으로 알파벳 대문자 또는 숫자 선택
            char randomChar = (char) ('A' + random.nextInt(26 + 10)); // 26 letters + 10 digits
            if (randomChar > 'Z') {
                randomChar = (char) ('0' + (randomChar - 'Z' - 1)); // Adjust to get digits
            }
            randomPart.append(randomChar);
        }

        // 타임스탬프 부분 (마지막 4자리)
        String timePart = String.format("%04d", timestamp % 10000);

        // 형식: 암호화된ID(가변길이) + 타임스탬프(4자리) + 랜덤문자(5자리)
        return idPart + timePart + randomPart.toString();
    }
}

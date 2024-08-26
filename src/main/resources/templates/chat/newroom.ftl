<!doctype html>
<html lang="en">
<head>
    <title>Websocket Chat</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <!-- CSS -->
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/dist/css/bootstrap.min.css">
    <style>
        [v-cloak] {
            display: none;
        }
    </style>
</head>
<body>
<div class="container" id="app" v-cloak>
    <div class="row">
        <div class="col-md-6">
            <h3>채팅방 리스트</h3>
        </div>
        <div class="col-md-6 text-right">
            <a class="btn btn-primary btn-sm" href="/chat/myroom">내 채팅방 가기</a>
            <a class="btn btn-primary btn-sm" href="/logout">로그아웃</a>
        </div>
    </div>
    <div class="input-group">
        <div class="input-group-prepend">
            <label class="input-group-text">방제목</label>
        </div>
        <input type="text" class="form-control" v-model="room_name" v-on:keyup.enter="createRoom">
        <div class="input-group-append">
            <button class="btn btn-primary" type="button" @click="createRoom">채팅방 개설</button>
        </div>
    </div>
    <ul class="list-group">
        <li class="list-group-item list-group-item-action" v-for="item in chatrooms" v-bind:key="item.roomId">
            <h6 @click="confirmEnterRoom(item.roomId, item.name)">
                {{ item.name }}
            </h6>
            <span class="badge badge-info badge-pill" @click="showUserList(item.roomId)">
                {{ item.userCount }}
            </span>
        </li>
    </ul>

    <!-- Modal for showing user list -->
    <ul v-if="users.length > 0" class="list-group mt-3">
        <li v-for="user in users" :key="user.username" class="list-group-item">
            {{ user.username }}
        </li>
    </ul>
    <p v-else class="mt-3">참여자가 없습니다.</p>

</div>
<!-- JavaScript -->
<script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
<script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
<script src="/webjars/bootstrap/4.3.1/dist/js/bootstrap.min.js"></script>
<script>
    var vm = new Vue({
        el: '#app',
        data: {
            room_name : '',
            chatrooms: [],
            users: [] // List to store users in a room
        },
        created() {
            this.findAllRoom();
        },
        methods: {
            findAllRoom: function() {
                axios.get('/chat/rooms').then(response => {
                    // Ensure the response is an array
                    if (Array.isArray(response.data)) {
                        this.chatrooms = response.data;
                    }
                });
            },
            createRoom: function() {
                if (this.room_name === "") {
                    alert("방 제목을 입력해 주십시요.");
                    return;
                } else {
                    var params = new URLSearchParams();
                    params.append("name", this.room_name);
                    axios.post('/chat/room', params)
                        .then(response => {
                            alert(response.data.name + " 방 개설에 성공하였습니다.");
                            this.room_name = '';
                            this.findAllRoom();
                        })
                        .catch(response => {
                            alert("채팅방 개설에 실패하였습니다.");
                        });
                }
            },
            confirmEnterRoom: function(roomId, roomName) {
                // Confirm dialog
                if (confirm(roomName + " 방으로 입장하시겠습니까?")) {
                    this.enterRoom(roomId, roomName);
                }
            },
            enterRoom: function(roomId, roomName) {
                localStorage.setItem('wschat.roomId', roomId);
                localStorage.setItem('wschat.roomName', roomName);
                location.href = "/chat/room/join/" + roomId;
            },
            showUserList: function(roomId) {
                // Fetch user list for the room
                axios.get('/chat/room/' + roomId + '/users')
                    .then(response => {
                        if (Array.isArray(response.data)) {
                            this.users = response.data;
                        }
                    })
                    .catch(error => {
                        alert("사용자 목록을 가져오는 데 실패하였습니다.");
                    });
            }
        }
    });
</script>
</body>
</html>

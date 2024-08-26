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
                <a class="btn btn-primary btn-sm" href="/chat/room">전체 채팅방 가기</a>
                <a class="btn btn-primary btn-sm" href="/logout">로그아웃</a>
            </div>
        </div>
        <ul class="list-group">
            <li class="list-group-item list-group-item-action" v-for="item in chatrooms" v-bind:key="item.roomId" v-on:click="enterRoom(item.roomId, item.name)">
                <h6>{{item.name}} <span class="badge badge-info badge-pill">{{item.userCount}}</span></h6>
            </li>
        </ul>
    </div>
    <!-- JavaScript -->
    <script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
    <script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
    <script>
        var vm = new Vue({
            el: '#app',
            data: {
                room_name : '',
                chatrooms: [
                ]
            },
            created() {
                this.findAllRoom();
            },
            methods: {
                findAllRoom: function() {
                    axios.get('/chat/myrooms').then(response => {
                        // prevent html, allow json array
                        if(Object.prototype.toString.call(response.data) === "[object Array]")
                            this.chatrooms = response.data;
                    });
                },
                enterRoom: function(roomId, roomName) {
                    localStorage.setItem('wschat.roomId',roomId);
                    localStorage.setItem('wschat.roomName',roomName);
                    location.href="/chat/room/enter/"+roomId;
                }
            }
        });
    </script>
  </body>
</html>
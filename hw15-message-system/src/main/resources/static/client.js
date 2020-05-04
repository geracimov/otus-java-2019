const stompClient = Stomp.over(new SockJS('/websocket'));

const addUser = () => {
    var message={};
    message.name=$("#input-name").val();
    message.login=$("#input-login").val();
    message.password=$("#input-password").val();
    stompClient.send("/app/user/save", {}, JSON.stringify(message))
}

const renderUsers = (users) => {
    if (Array.isArray(users)){
        console.log('isArray');
        users.forEach(user => renderUser(user));
    } else {
        console.log('not isArray');
        renderUser(users);
    }
}

function renderUser(user){
    if (user != null){
        $("#user-table > tbody").append("<tr><td>" + user.id + "</td><td>" + user.name + "</td><td>" + user.login + "</td><td>" + user.password + "</td></tr>");
    }
}

$(()=> {
        $("form").on('submit', (event) => event.preventDefault());
        $("#send-button").click(addUser);

        stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/ws/user/queue'  , (response) => {
                renderUsers(JSON.parse(response.body));
            });
            stompClient.subscribe('/ws/user/errors'  , (response) => {
                alert(response.body);
            });
            stompClient.send("/app/user");
        });
});

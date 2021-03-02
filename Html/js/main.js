//carrega mensagens assim que a pagina inicia e limpa a caixa de texto do input
document.addEventListener("DOMContentLoaded", function() {
  getUsers();
  getMessages();
  curUser = null
  document.getElementById('input').value = ""
});

//variavel para saber qual o utilizador ativo
var CurUser


//a cada segundo (1000 sao em milisegundos), atualiza as mensagens, a cada segundo, 
//mas com meio segundo de distancia(em contra tempo), atualiza os utilizadores, para nao haver troca de informações
var intervalID = setInterval(function(){
	getMessages();
	
	setTimeout(function(){
    	getUsers();
	}, 500);}, 
1000);


//fazer um get ao servidor
function httpGet(url, callback){
	const request = new XMLHttpRequest();
	request.open('get', url, true);
	request.onload = function(){
		callback(request);
	};

	request.send();
}


//defenir o utilizador atual
function setUser(){	
	var nickname = document.getElementById('input').value; //nome do utilizador
	var type = document.getElementById('typeSelect').value; //tipo de utilizador
	if (nickname.length !== 0) {
	fetch("http://localhost:8081/newUser_field:" + nickname + "_field:" + type, {method: "POST"}) //pedido post
		.then(function(response){
			return(response.text().then(function(text){ //resposta 
				curUser = text; //id do utilizador
				document.getElementById('input').value = "";
				Swal.fire(
	    				  'Nickname registado com sucesso',
	    				  'Bem-vindo!',
	    				  'success'
	    				)
	    				document.getElementById("registo").disabled = true;
						input.placeholder = "Escreve aqui a tua mensagem ...";
			}));
		});
} else {Swal.fire(
		  'Escreve um nickname válido!') 
	}
}

//enviar uma mensagem
function setMsg(){
	var msg = document.getElementById('input').value; //conteudo da mensagem
	if(curUser == null){ //se nao houver um utilizador ativo
		Swal.fire(
				  'Não existe um utilizador ativo!'
				)
}
	else if (msg.length !== 0){
		fetch("http://localhost:8081/newMessage_field:" + msg + "_field:" + curUser, {method: "POST"})
			.then(function(response){
				return(response.text().then(function(text){
					document.getElementById('input').value = ""; //limpar a caixa de input
					Swal.fire({
						  title: 'Mensagem enviada!',
						  html: '',
						  timer: 500,
						  timerProgressBar: true,
						  onBeforeOpen: () => {
						    Swal.showLoading()
						    timerInterval = setInterval(() => {
						      Swal.getContent().querySelector('b')
						        .textContent = Swal.getTimerLeft()
						    }, 30)
						  },
						  onClose: () => {
						    clearInterval(timerInterval)
						  }
						}).then((result) => {
						  if (
						    /* Read more about handling dismissals below */
						    result.dismiss === Swal.DismissReason.timer
						  ) {
						    console.log('I was closed by the timer') // eslint-disable-line
						  }
						})
				}));
			});
		} else {
			Swal.fire(
		
				  'Convém escrever algo, nao?!!'
		) 
}
}

function getUsers(){
	httpGet("http://localhost:8081/getUsers", function(request){
		document.getElementById('usersTextArea').value = decodeURI(request.responseText); //ir buscar todos os utilizadores
	});
}

function getMessages(){
	httpGet("http://localhost:8081/getMessages", function(request){
		document.getElementById('messagesTextArea').value = decodeURI(request.responseText); // ir buscar todas as mensagens
	});
}


function logOut(){
	if(curUser == null){ //se nao houver um utilizador ativo
		Swal.fire(
				  'Não existe um utilizador ativo!'
				)
	}
	else{
		
					Swal.fire({
						title: 'Tem a certeza que deseja sair da conversa?',
						icon: 'warning',
						showCancelButton: true,
						confirmButtonColor: '#3085d6',
						cancelButtonColor: '#d33',
						 confirmButtonText: 'Sim, sair!'
					}).then((result) => {
						if (result.value) {
						 Swal.fire(
							  'Saiu!',
								 'Saiu com sucesso.',
								  )
								  	fetch("http://localhost:8081/logOut_field:" + curUser, {method: "POST"})
								  	.then(function(response){
								  		return(response.text().then(function(text){
								  			curUser = null; //fazer o log out, ou seja, defenir que nao ha um utilizador ativo
								  			alert("Log out efetuado com sucesso"); //caixa pop up
								  			}));
								  				});
								 					location.reload();
									
						}
							})
			
		}
}


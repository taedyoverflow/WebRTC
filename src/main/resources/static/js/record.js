let isRecording = false;
let roomName = "";
let mediaRecorder;
let audioChunks = [];
let fileIndex = 1; // 파일 인덱스를 1부터 시작
let recordingInterval;

$(document).ready(function() {
    let id = $("#id").val();
    let uuid = $("#uuid").val();
    $("#roomInfo").text("User: " + uuid + " @ Room #" + id);
    roomName = "Room_" + id;
});

document.getElementById("recordButton").addEventListener("click", function() {
    if (!isRecording) {
        startRecording();
    } else {
        stopRecording();
    }
});

function startRecording() {
    navigator.mediaDevices.getUserMedia({ audio: true })
        .then(stream => {
            mediaRecorder = new MediaRecorder(stream);
            audioChunks = [];
            mediaRecorder.ondataavailable = event => {
                audioChunks.push(event.data);
            };
            mediaRecorder.onstop = uploadAudio;
            mediaRecorder.start();
            isRecording = true;
            document.getElementById("recordButton").innerText = "종료";
            document.getElementById("recordButton").classList.remove("btn-outline-success");
            document.getElementById("recordButton").classList.add("btn-outline-danger");

            // 10초마다 자동 저장 로직
            recordingInterval = setInterval(() => {
                if (mediaRecorder.state === "recording") {
                    mediaRecorder.stop(); // 녹음 중지
                }
                audioChunks = []; // audioChunks 초기화
                mediaRecorder.start(); // 새로운 녹음 시작
            }, 10000);
        })
        .catch(error => console.error("Error accessing the microphone:", error));
}

function stopRecording() {
    clearInterval(recordingInterval); // 자동 저장 인터벌 중지
    if (mediaRecorder.state === "recording") {
        mediaRecorder.stop(); // 녹음 중지
    }
    isRecording = false;
    document.getElementById("recordButton").innerText = "녹음 시작";
    document.getElementById("recordButton").classList.remove("btn-outline-danger");
    document.getElementById("recordButton").classList.add("btn-outline-success");
}

function uploadAudio() {
    const audioBlob = new Blob(audioChunks, {type: 'audio/wav'});
    const formData = new FormData();
    let fileName = `${roomName}_${fileIndex}.wav`;
    formData.append("audioFile", audioBlob, fileName);

    fetch("/file/send-audio", {
        method: "POST",
        body: formData,
    })
        .then(response => response.text())
        .then(data => console.log(data))
        .catch(error => console.error("Upload failed:", error));

    fileIndex++; // 파일 인덱스 증가
}

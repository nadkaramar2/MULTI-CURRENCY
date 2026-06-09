import Axios from "axios";
export default Axios.create({
  // baseURL: `https://ams-qa.montra.org/AmsAPI/`,
  // baseURL: "http://103.11.153.212:8185/AmsAPI/",
     baseURL: "http://103.11.153.212:8385/AmsAPI/",       // Multi Currency URL
    // baseURL: "http://103.11.153.212:8785/AmsAPI/",           //Credi Card URL
  participantId: sessionStorage.getItem("Participantid"),
});

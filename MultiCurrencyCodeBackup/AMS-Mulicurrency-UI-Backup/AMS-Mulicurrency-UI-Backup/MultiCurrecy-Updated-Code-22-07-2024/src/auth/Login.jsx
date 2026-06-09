import React, { useState, useEffect } from "react";
import { ArrowPathIcon, LockClosedIcon } from "@heroicons/react/24/solid";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import TranEco from "../assets/img/TranEco.png";
import amsApi from "../api/amsApi";
import CustomAlert from "../layout/CustomAlert";
import swal from "sweetalert";
import { Link } from "react-router-dom";
const Login = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { error, authInfo, loading } = useSelector((state) => state.auth);
  const [showAlert, setShowAlert] = useState(false);

  const [alertTitle, setAlertTitle] = useState("");
  const [checkrole, setCheckRole] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  let existancerole = "true";
  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };
  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };
  const handleCloseAlert = () => {
    setShowAlert(false);
  };
  // UNIQUE DEVICE ID CREATING FROM HERE
  var today = new Date();
  let dd = today.getDate();
  if (dd < 10) dd = "0" + dd;
  let mm = today.getMonth() + 1;
  if (mm < 10) mm = "0" + mm;
  var date = dd + "" + mm + "" + today.getFullYear();
  let hour = today.getHours();
  if (hour < 10) hour = "0" + hour;
  let minute = today.getMinutes();
  if (minute < 10) minute = "0" + minute;
  var time = hour + "" + minute;
  var dateTime = date + "" + time;
  const AppName = "AMS_";
  const AppEndName = "_AMS";
  const [allData, setAllData] = useState([]);
  const [paricipant, setParicipant] = useState("");
  useEffect(() => {
    showAPI();
  }, []);
  const showAPI = async () => {
    try {
      const response = await amsApi.post(
        `apiky`,
        {
          device_Id: `${AppName}${dateTime}${AppEndName}`,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.response.code === "S0000") {
        localStorage.setItem("token", response.data.token);
      } else {
        handleShowError(response.data.message);
        showAPI();
      }
    } catch (error) {
      handleShowError(error.response.data.message);
      showAPI();
    }
  };

  useEffect(() => {
    if (error) {
      handleShowError(error);
    }
    if (authInfo !== null) {
      navigate("/dashboard", { replace: true });
    }
  }, [dispatch, error, authInfo, navigate]);
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");
  const storedToken = localStorage.getItem("token");
  const [checkUser, setCheckUser] = useState([]);
  console.log(checkUser);
  const checkUserIdHandler = async () => {
    try {
      const response = await amsApi.post(
        `/participant_master/getAllParticipantList`,
        {
          loginid: userId,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setCheckUser(response.data.isParticipantListEmpty);
        setAllData(response.data.participantMasterObj);
        setCheckRole(response.data.isParticipantListEmpty);
      }
    } catch (error) {
      showAPI();
      console.log("Something went wrong");
    }
  };

  const loginHandler = async (event) => {
    event.preventDefault();
    if (userId.trim().length === 0 || password.trim().length === 0) {
      swal("Please enter a valid Credentials (non-empty values).");
    }
    try {
      const formData = new FormData();
      formData.append("userId", userId);
      formData.append("password", password);
      formData.append("participantId", paricipant || "0");
      localStorage.setItem("userName", userId);
      sessionStorage.setItem("Participantid", paricipant);
      const res = await amsApi.post(`/encryptedData`, formData, {
        headers: {
          "Content-Type": "application/json",
          apikey:
            "rO0ABXQANU1PTjIzMjA3MDAwMDAwMV8kTU9OVFJBXyQyNl8kSnVsXyQyMDIzXyQxNjkwMzcwMDM0OTY4EPARATORMON232070000001",
          Authorization: `${storedToken}`,
        },
      });
      if (res.status === 200) {
        const storedToken = localStorage.getItem("token");

        const response = await amsApi.post(
          `/signIn`,
          {
            salt: res.data.salt,
            payload: res.data.payload,
          },
          {
            headers: {
              "Content-Type": "application/json",
              IS_WEB_CALL: "Y",
              apikey:
                "rO0ABXQANU1PTjIzMjA3MDAwMDAwMV8kTU9OVFJBXyQyNl8kSnVsXyQyMDIzXyQxNjkwMzcwMDM0OTY4EPARATORMON232070000001",
              Authorization: `Bearer ${storedToken}`,
            },
          }
        );
        if (response.data.code === "S0000") {
          const storedToken = localStorage.getItem("token");
          const response1 = await amsApi.post(
            `deccryptedData`,
            {
              salt: response.data.salt,
              payload: response.data.payload,
            },
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${storedToken}`,
              },
            }
          );
          if (response1.data.code === "S0000") {
            localStorage.setItem("authInfo", JSON.stringify(response1.data));

            navigate("/dashboard", { replace: true });
            window.location.reload();
          } else {
            showAPI();
            handleShowError(response1.data.message);
          }
        } else {
          handleShowError("Error Code : " + response.data.code);
          showAPI();
        }
      } else if (res.status === 401) {
        handleShowError(res.data.error);
        console.log(res.data.error);
        showAPI();
      }
    } catch (error) {
      handleShowError(error.res.data.error);
      showAPI();
    }
  };

  return (
    <>
      <div
        className="flex flex-col items-center justify-center  mx-auto lg:h-screen"
        style={{ backgroundColor: "#2b447b" }}
      >
        <div className="flex items-center justify-center py-3 px-4 sm:px-6 lg:px-8 border-2 bg-white  rounded-md ">
          <div className="max-w-md w-full space-y-2 ">
            <div className="flex justify-center">
              <img alt="logo" className="h-16 w-auto mx-28" src={TranEco} />
            </div>

            <div className=" space-y-2 md:space-y-2 sm:p-2">
              <div className="items-center justify-center">
                <h2 className="text-2xl  font-bold mb-4 text-center">
                  Login to your account
                </h2>
              </div>{" "}
              <form
                className=" md:space-y-2"
                action="#"
                onSubmit={loginHandler}
                autoComplete="off"
              >
                <div>
                  <label
                    htmlFor="userId"
                    className="block text-sm font-semibold leading-6 text-gray-900"
                  >
                    Username
                  </label>
                  <input
                    id="userId"
                    name="userId"
                    type="text"
                    value={userId}
                    onChange={(e) => {
                      setUserId(e.target.value.trim());
                    }}
                    autoComplete="off"
                    required
                    // ...other attributes

                    className="block w-full rounded-md border-0 px-3.5 py-1 bg-slate-50 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400  focus:outline-none  focus:z-10 focus:ring-indigo-600 sm:text-sm sm:leading-6"
                    placeholder="Username"
                  />
                </div>
                <div className="relative">
                  <div>
                    <label
                      htmlFor="password"
                      className="block  text-sm font-medium text-gray-900 dark:text-white"
                    >
                      Password
                    </label>
                    <input
                      id="password"
                      name="password"
                      type={showPassword ? "text" : "password"}
                      // onKeyUp={checkUserIdHandler}
                      onFocus={checkUserIdHandler}
                      value={password}
                      onChange={(e) => setPassword(e.target.value.trim())}
                      autoComplete="new-password"
                      placeholder="••••••••"
                      className="block w-full rounded-md border-0 px-3.5 py-1 bg-slate-50 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400  focus:outline-none  focus:z-10 focus:ring-indigo-600 sm:text-sm sm:leading-6"
                      required=""
                    />
                  </div>

                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute inset-y-0 right-0  top-6 flex items-center pr-2"
                  >
                    {showPassword ? (
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        fill="none"
                        viewBox="0 0 24 24"
                        strokeWidth={2}
                        className="w-6 h-6  stroke-indigo-900"
                      >
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          d="M3.98 8.223A10.477 10.477 0 001.934 12C3.226 16.338 7.244 19.5 12 19.5c.993 0 1.953-.138 2.863-.395M6.228 6.228A10.45 10.45 0 0112 4.5c4.756 0 8.773 3.162 10.065 7.498a10.523 10.523 0 01-4.293 5.774M6.228 6.228L3 3m3.228 3.228l3.65 3.65m7.894 7.894L21 21m-3.228-3.228l-3.65-3.65m0 0a3 3 0 10-4.243-4.243m4.242 4.242L9.88 9.88"
                        />
                      </svg>
                    ) : (
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        fill="none"
                        viewBox="0 0 24 24"
                        strokeWidth={2}
                        className="w-6 h-6  stroke-indigo-900"
                      >
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          d="M2.036 12.322a1.012 1.012 0 010-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178z"
                        />
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                        />
                      </svg>
                    )}
                  </button>
                </div>
                {(checkUser === "false" || existancerole !== checkrole) && (
                  <div>
                    <label htmlFor="strParticipantId" className="">
                      Participant
                    </label>
                    <select
                      id="strParticipantId"
                      name="strParticipantId"
                      value={paricipant}
                      onChange={(e) => setParicipant(e.target.value)}
                      className="block w-full rounded-md border-0 px-3.5 py-1 bg-slate-50 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400  focus:outline-none  focus:z-10 focus:ring-indigo-600 sm:text-sm sm:leading-6"
                      required
                    >
                      <option value="">Select</option>
                      {allData?.map((data) => (
                        <option
                          className="capatlize text-lg"
                          value={data.strParticipantID || "0"}
                        >
                          {data.strParticipantName || "0"}
                        </option>
                      ))}
                    </select>
                  </div>
                )}
                <div>
                  {/* <button
                    title="Login Here"
                    disabled={loading ? true : false}
                    type="submit"
                    className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-base font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 cursor-pointer"
                  >
                    <span className="absolute left-0 inset-y-0 flex items-center pl-3">
                      {loading ? (
                        <ArrowPathIcon className="h-5 w-5 text-blue-200 rotate-180  group-hover:text-blue-300" />
                      ) : (
                        <LockClosedIcon
                          className="h-5 w-5 text-blue-200 group-hover:text-blue-300"
                          aria-hidden="true"
                        />
                      )}
                    </span>
                    Login
                  </button> */}
                  <button
                    title="Login Here"
                    disabled={loading ? true : false}
                    type="submit"
                    className="group relative w-full flex justify-center py-1 px-4 border border-transparent text-base font-medium rounded-md text-white bg-indigo-900 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 cursor-pointer"
                  >
                    <span className="absolute left-0 inset-y-0 flex items-center pl-3">
                      {loading ? (
                        <ArrowPathIcon className="h-5 w-5 text-blue-200 rotate-180  group-hover:text-blue-300" />
                      ) : (
                        <LockClosedIcon
                          className="h-5 w-5 text-blue-200 group-hover:text-blue-300"
                          aria-hidden="true"
                        />
                      )}
                    </span>
                    Login
                  </button>
                </div>
                <div className="flex items-center justify-between mx-2 underline">
                  <Link to={`/password`}>
                    <button className="text-sm font-medium text-blue-700 hover:underline dark:text-primary-500">
                      Forgot password?
                    </button>
                  </Link>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
      {/* </div> */}
      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </>
  );
};
export default Login;

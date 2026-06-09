import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";

import amsApi from "../../api/amsApi";
import Swal from "sweetalert2";
import { useSelector } from "react-redux";
import CustomAlert from "../../layout/CustomAlert";
import swal from "sweetalert";

export default function CreateUser() {
  const { authInfo } = useSelector((state) => state.auth);
  const UserId = authInfo.userId;
  const [rollData, setrollData] = useState([]);
  const [participantData, setParticipantData] = useState([]);
  const [alldata3, setalldata3] = useState([]);
  const [alldata5, setalldata5] = useState([]);
  const [alldata6, setalldata6] = useState([]);
  const [strUserId, setstrUserId] = useState("");
  const [strPassword, setstrPassword] = useState("");
  const [strTitle, setstrTitle] = useState("");
  const [strFirstName, setstrFirstName] = useState("");
  const [strMiddleName, setstrMiddleName] = useState("");
  const [strLastName, setstrLastName] = useState("");
  const [strGender, setstrGender] = useState("");
  const [strEmailID, setstrEmailID] = useState("");
  const [strMobileNo, setstrMobileNo] = useState("");
  const [strAddress1, setstrAddress1] = useState("");
  const [strAddress2, setstrAddress2] = useState("");
  const [strAddress3, setstrAddress3] = useState("");
  const [strPinCode, setstrPinCode] = useState("");
  const [strCity, setstrCity] = useState("");
  const [strState, setstrState] = useState("");
  const [strCountry, setstrCountry] = useState("");
  const [strCountryCode, setstrCountryCode] = useState("");
  const [strRoleName, setStrRoleName] = useState("");
  const [strParticipantName, setStrParticipantName] = useState("");

  // let useid = localStorage.getItem("userName");

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

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
  const showSuccess = (resMessage) => {
    Swal.fire({
      title: "Success",
      text: resMessage,
      allowOutsideClick: false,
      icon: "success",
      confirmButtonText: "OK",
    });
  };
  const showError = (resMessage) => {
    Swal.fire({
      text: resMessage,
      allowOutsideClick: false,
      icon: "error",
      title: "Oops...",
      confirmButtonText: "OK",
    });
  };

  // PARTICIPANT DATA

  useEffect(() => {
    participantIdHandler();
  }, []);

  const participantIdHandler = async () => {
    try {
      const response = await amsApi.post(
        `/participant_master/getAllParticipants`,
        {
          loginid: UserId,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setParticipantData(response.data.participantMasterObj);
      } else {
      }
    } catch (error) {}
  };

  const rollId = async (participant) => {
    try {
      const response = await amsApi.post(`roleMaster/getRoles`, {
        strParticipantId: participant,
      });
      if (response.data.code === "S0000") {
        setrollData(response.data.roleList);
      } else {
      }
    } catch (error) {}
  };

  // COUNTRY
  useEffect(() => {
    country();
  }, [strCountry]);

  const country = async () => {
    try {
      const response = await amsApi.post(`address/getCountrylist`, {});

      if (response.data.code === "S0000") {
        setalldata3(response.data.countryList);
      } else {
      }
    } catch (error) {}
  };

  // STATE
  const state = async (data) => {
    try {
      const response = await amsApi.post(`address/getStatelist`, {
        strCountryID: data,
      });

      if (response.data.code === "S0000") {
        setalldata5(response.data.stateList);
      } else {
      }
    } catch (error) {}
  };

  // CITY
  const city = async (data) => {
    try {
      const response = await amsApi.post(`address/getCitylist`, {
        strStateID: data,
      });

      if (response.data.code === "S0000") {
        setalldata6(response.data.cityList);
      } else {
      }
    } catch (error) {}
  };

  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [errorMessage1, setErrorMessage1] = useState("");
  const handleSubmit = async () => {
    if (
      strParticipantName === "" ||
      strRoleName === "" ||
      strUserId === "" ||
      strPassword === "" ||
      strTitle === "" ||
      strFirstName === "" ||
      strGender === "" ||
      strEmailID === "" ||
      strMobileNo === "" ||
      strAddress1 === "" ||
      strPinCode === "" ||
      strCity === "" ||
      strState === "" ||
      strCountry === "" ||
      strCountryCode === ""
    ) {
      setError(true);
    } else if (!UserId) {
      swal("Please check your User id");
    } else {
      try {
        const response = await amsApi.post(`usermaster/saveusermaster`, {
          strParticipantName: strParticipantName,
          strRoleId: strRoleName,
          strUserId: strUserId,

          strPassword: strPassword,
          strTitle: strTitle,
          strFirstName: strFirstName,
          strMiddleName: strMiddleName,
          strLastName: strLastName,
          strGender: strGender,
          strEmailID: strEmailID,
          strMobileNo: strMobileNo,
          strAddress1: strAddress1,
          strAddress2: strAddress2,
          strAddress3: strAddress3,
          strPinCode: strPinCode,
          strCity: strCity,
          strState: strState,
          strCountry: strCountry,
          strCreatedBy: UserId,
          strCountryCode: strCountryCode,
        });
        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          setError("");
          handleClick();
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };

  const [showPassword, setShowPassword] = useState(false);
  const handleClickShowPassword = () => setShowPassword((show) => !show);
  const handleMouseDownPassword = (event) => {
    event.preventDefault();
  };

  const handleClick = () => {
    setstrUserId("");

    setstrPassword("");
    setstrTitle("");
    setstrFirstName("");
    setstrMiddleName("");
    setstrLastName("");
    setstrCity("");
    setstrAddress1("");
    setstrAddress2("");
    setstrCountry("");
    setstrState("");
    setstrCountryCode("");
    setstrEmailID("");
    setstrGender("");
    setstrMiddleName("");
    setstrMobileNo("");
    setstrPinCode("");
  };

  const [errorMessage2, setErrorMessage2] = useState("");

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-1 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Create User
              </p>
            </div>
          </div>
        </div>
        <div className="max-w-7xl mx-auto h-full overflow-auto max-h-full lg:max-h-[30rem]">
          <div className="overflow-hidden shadow ">
            <div className=" px-4 py-1 sm:p-4 bg-white">
              <div className="grid md:grid-cols-4 md:gap-3 ">
                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    Participant Name
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    id="strParticipantName"
                    name="strParticipantName"
                    value={strParticipantName}
                    onChange={(e) =>
                      setStrParticipantName(
                        e.target.value,
                        rollId(e.target.value)
                      )
                    }
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  >
                    <option value="">Select</option>
                    {participantData.map((data) => (
                      <option
                        className="capatlize text-sm"
                        value={data.strParticipantName}
                      >
                        {data.strParticipantName || ""}
                      </option>
                    ))}
                  </select>
                  {error && strParticipantName.length <= 0 ? (
                    <p className="text-red-500   text-sm font-medium">
                      Please Enter ParticipantId
                    </p>
                  ) : (
                    ""
                  )}
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    Role Name
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    id="strRoleName"
                    name="strRoleName"
                    autoComplete="off"
                    value={strRoleName}
                    onChange={(e) => setStrRoleName(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  >
                    <option> Select </option>
                    {rollData.map((data) => (
                      <option
                        className="capatlize text-sm"
                        value={data.strRoleId}
                      >
                        {data.strRoleName}
                      </option>
                    ))}
                  </select>
                  {error && strRoleName.length <= 0 ? (
                    <p className="text-red-500 text-sm font-medium">
                      Please Enter RoleId!
                    </p>
                  ) : (
                    ""
                  )}
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    User Id <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="strUserId"
                    name="strUserId"
                    autoComplete="off"
                    maxLength={40}
                    value={strUserId}
                    onChange={(e) => setstrUserId(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="User Id"
                  />
                  {error && strUserId.length <= 0 ? (
                    <p className="text-red-500   text-sm font-medium">
                      Please Enter User Id
                    </p>
                  ) : (
                    ""
                  )}
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    Password <span className="text-red-600 px-2">*</span>
                  </label>
                  <div className="flex ">
                    <input
                      name="strPassword"
                      id="strPassword"
                      autoComplete="new-password"
                      value={strPassword}
                      // onChange={(e) => setstrPassword(e.target.value)}
                      // onChange={handlePassword}
                      // onBlur={validatePassword}
                      type={showPassword ? "text" : "password"}
                      onChange={(e) => {
                        const value = e.target.value;
                        setstrPassword(value);

                        const regex =
                          /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]).{8,}$/;
                        if (!regex.test(value)) {
                          setErrorMessage2(
                            "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
                          );
                        } else {
                          setErrorMessage2("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (strPassword.length === 0) {
                          setError("Please Enter Password!");
                          setErrorMessage2("");
                        }
                      }}
                      placeholder="Password"
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                    <div
                      className="bg-blue-100"
                      onClick={handleClickShowPassword}
                      onMouseDown={handleMouseDownPassword}
                    >
                      {showPassword ? (
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          fill="none"
                          viewBox="0 0 24 24"
                          strokeWidth={1.5}
                          stroke="currentColor"
                          className="w-8 h-6 mt-1"
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
                      ) : (
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          fill="none"
                          viewBox="0 0 24 24"
                          strokeWidth={1.5}
                          stroke="currentColor"
                          className="w-8 h-6 mt-1"
                        >
                          <path
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            d="M3.98 8.223A10.477 10.477 0 001.934 12C3.226 16.338 7.244 19.5 12 19.5c.993 0 1.953-.138 2.863-.395M6.228 6.228A10.45 10.45 0 0112 4.5c4.756 0 8.773 3.162 10.065 7.498a10.523 10.523 0 01-4.293 5.774M6.228 6.228L3 3m3.228 3.228l3.65 3.65m7.894 7.894L21 21m-3.228-3.228l-3.65-3.65m0 0a3 3 0 10-4.243-4.243m4.242 4.242L9.88 9.88"
                          />
                        </svg>
                      )}
                    </div>
                  </div>

                  {errorMessage2 ? (
                    <p className="text-red-500 text-sm font-medium">
                      {errorMessage2}
                    </p>
                  ) : error && strPassword.length <= 0 ? (
                    <p className="text-red-500 text-sm font-medium">
                      Please Enter Password!
                    </p>
                  ) : null}
                  {/* {error && strPassword.length <= 0 ? (
                      <p className="text-red-500   text-sm font-medium">
                        Please Enter Password
                      </p>
                    ) : (
                      ""
                    )} */}
                </div>
              </div>
              <div>
                <h2 className="font-normal md:font-bold  mt-1">
                  Enter User Details :
                </h2>
              </div>
              <div className="grid md:grid-cols-4 md:gap-3">
                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    Title <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    id="strTitle"
                    name="strTitle"
                    value={strTitle}
                    onChange={(e) => setstrTitle(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  >
                    <option value=""> Select </option>
                    <option value="Mr"> Mr.</option>
                    <option value="Mrs">Mrs.</option>
                  </select>
                  {error && strTitle.length <= 0 ? (
                    <p className="text-red-500   text-sm font-medium">
                      Please Select Title
                    </p>
                  ) : (
                    ""
                  )}
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    First Name <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    autoComplete="off"
                    id="strFirstName"
                    name="strFirstName"
                    value={strFirstName}
                    onChange={(e) => setstrFirstName(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Enter First Name"
                  />
                  {error && strFirstName.length <= 0 ? (
                    <p className="text-red-500 text-sm font-medium">
                      Please Enter FirstName
                    </p>
                  ) : (
                    ""
                  )}
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    Middle Name
                  </label>
                  <div className="mt-1 flex rounded-md shadow-sm">
                    <input
                      type="text"
                      autoComplete="off"
                      id="strMiddleName"
                      name="strMiddleName"
                      value={strMiddleName}
                      onChange={(e) => setstrMiddleName(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Middle Name"
                    />
                  </div>
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    Last Name <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    autoComplete="off"
                    id="strLastName"
                    name="strLastName"
                    value={strLastName}
                    onChange={(e) => setstrLastName(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Enter Last Name"
                  />
                  {error && strLastName.length <= 0 ? (
                    <p className="text-red-500   text-sm font-medium">
                      Please Enter LastName
                    </p>
                  ) : (
                    ""
                  )}
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    Gender <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    id="strGender"
                    autoComplete="off"
                    name="strGender"
                    value={strGender}
                    onChange={(e) => setstrGender(e.target.value)}
                  >
                    <option value=""> Select </option>
                    <option value="M">Male</option>
                    <option value="F">Female</option>
                  </select>
                  {error && strGender.length <= 0 ? (
                    <p className="text-red-500   text-sm font-medium">
                      Please Select Gender
                    </p>
                  ) : (
                    ""
                  )}
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    Email <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    autoComplete="off"
                    id="strEmailID"
                    name="strEmailID"
                    value={strEmailID}
                    onChange={(e) => setstrEmailID(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Enter Email Id"
                  />
                  {error && strEmailID.length <= 0 ? (
                    <p className="text-red-500 text-sm font-medium">
                      Please Enter EmailID
                    </p>
                  ) : (
                    ""
                  )}
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    Country Code <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    autoComplete="off"
                    id="strCountryCode"
                    name="strCountryCode"
                    value={strCountryCode}
                    onChange={(e) => setstrCountryCode(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Enter Country Code"
                  />
                  {error && strCountryCode.length <= 0 ? (
                    <p className="text-red-500   text-sm font-medium">
                      Please Enter CountryCode
                    </p>
                  ) : (
                    ""
                  )}
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    Mobile No <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="strMobileNo"
                    name="strMobileNo"
                    autoComplete="off"
                    maxLength={15}
                    minLength={10}
                    value={strMobileNo}
                    // onChange={(e) => setstrMobileNo(e.target.value)}
                    onChange={(e) => {
                      const value = e.target.value;
                      setstrMobileNo(value);

                      const regex = /^[0-9]*$/;
                      if (!regex.test(value)) {
                        setErrorMessage(" Only allow numeric digits");
                      } else {
                        setErrorMessage("");
                      }
                      setError("");
                    }}
                    onBlur={() => {
                      if (strMobileNo.length === 0) {
                        setError("Please Enter MobileNo!");
                        setErrorMessage("");
                      }
                    }}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Enter Mobile No"
                  />
                  {errorMessage ? (
                    <p className="text-red-500 text-sm font-medium">
                      {errorMessage}
                    </p>
                  ) : error && strMobileNo.length <= 0 ? (
                    <p className="text-red-500 text-sm font-medium">
                      Please Enter MobileNo!
                    </p>
                  ) : null}
                  {/* {error && strMobileNo.length <= 0 ? (
                      <p className="text-red-500   text-sm font-medium">
                        Please Enter MobileNo
                      </p>
                    ) : (
                      ""
                    )} */}
                </div>
              </div>
              <div>
                <h2 className="font-normal md:font-bold  mt-1">
                  Address Details
                </h2>
              </div>
              <div className="grid md:grid-cols-4 md:gap-4 ">
                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    Address 1 <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    autoComplete="off"
                    id="strAddress1"
                    name="strAddress1"
                    value={strAddress1}
                    onChange={(e) => setstrAddress1(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Enter Address1"
                  />
                  {error && strAddress1.length <= 0 ? (
                    <p className="text-red-500   text-sm font-medium">
                      Please Enter Address1
                    </p>
                  ) : (
                    ""
                  )}
                </div>

                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    Address 2
                  </label>
                  <input
                    type="text"
                    autoComplete="off"
                    id="strAddress2"
                    name="strAddress2"
                    value={strAddress2}
                    onChange={(e) => setstrAddress2(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Enter Address 2"
                  />
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    Address 3
                  </label>
                  <div className="mt-1 flex rounded-md shadow-sm">
                    <input
                      type="text"
                      autoComplete="off"
                      id="strAddress3"
                      name="strAddress3"
                      value={strAddress3}
                      onChange={(e) => setstrAddress3(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="EnterAddress 3"
                    />
                  </div>
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    Pin Code <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    autoComplete="off"
                    id="strPinCode"
                    name="strPinCode"
                    maxLength={10}
                    value={strPinCode}
                    // onChange={(e) => setstrPinCode(e.target.value)}
                    onChange={(e) => {
                      const value = e.target.value;
                      setstrPinCode(value);

                      const regex = /^[0-9]*$/;
                      if (!regex.test(value)) {
                        setErrorMessage1(" Only allow numeric digits");
                      } else {
                        setErrorMessage1("");
                      }
                      setError("");
                    }}
                    onBlur={() => {
                      if (strPinCode.length === 0) {
                        setError("Please Enter PinCode!");
                        setErrorMessage1("");
                      }
                    }}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Enter Pincode"
                  />
                  {errorMessage1 ? (
                    <p className="text-red-500 text-sm font-medium">
                      {errorMessage1}
                    </p>
                  ) : error && strPinCode.length <= 0 ? (
                    <p className="text-red-500 text-sm font-medium">
                      Please Enter PinCode!
                    </p>
                  ) : null}
                  {/* {error && strPinCode.length <= 0 ? (
                      <p className="text-red-500   text-sm font-medium">
                        Please Enter PinCode
                      </p>
                    ) : (
                      ""
                    )} */}
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    Country <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    name="strCountry"
                    id="strCountry"
                    value={strCountry}
                    onChange={(e) =>
                      setstrCountry(e.target.value, state(e.target.value))
                    }
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  >
                    <option value="">Select</option>
                    {alldata3.map((data) => (
                      <option
                        className="capatlize text-lg"
                        value={data.countryId}
                      >
                        {data.countryName}
                      </option>
                    ))}
                  </select>
                  {error && strCountry.length <= 0 ? (
                    <p className="text-red-500   text-sm font-medium">
                      Please Select Country
                    </p>
                  ) : (
                    ""
                  )}
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    State <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    id="strState"
                    name="strState"
                    value={strState}
                    onChange={(e) =>
                      setstrState(e.target.value, city(e.target.value))
                    }
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  >
                    <option value="">Select</option>
                    {alldata5.map((data) => (
                      <option
                        className="capatlize text-lg"
                        value={data.stateId}
                      >
                        {data.strState}
                      </option>
                    ))}
                  </select>
                  {error && strState.length <= 0 ? (
                    <p className="text-red-500   text-sm font-medium">
                      Please Select State
                    </p>
                  ) : (
                    ""
                  )}
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block text-sm font-semibold text-gray-700"
                  >
                    City <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    id="strCity"
                    name="strCity"
                    value={strCity}
                    onChange={(e) => setstrCity(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  >
                    <option value="">Select</option>
                    {alldata6.map((data) => (
                      <option className="capatlize text-lg" value={data.cityId}>
                        {data.cityName}
                      </option>
                    ))}
                  </select>
                  {error && strCity.length <= 0 ? (
                    <p className="text-red-500   text-sm font-medium">
                      Please Select City
                    </p>
                  ) : (
                    ""
                  )}
                </div>
              </div>
            </div>
          </div>
          <div className="px-4 py-1 space-x-4 bg-gray-50 text-right sm:px-6">
            <button
              title="Click Submit Button"
              type="submit"
              onClick={handleSubmit}
              className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white text-sm   py-1 px-2 border border-blue-700 rounded"
            >
              Submit
            </button>
            <button
              title="Clear Data"
              type="button"
              onClick={handleClick}
              className="bg-red-500 hover:bg-red-700 focus:ring-2 focus:ring-red-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-red-700 rounded"
            >
              Clear
            </button>
          </div>
        </div>
      </div>
      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
      {/* </div> */}
    </AppLayout>
  );
}

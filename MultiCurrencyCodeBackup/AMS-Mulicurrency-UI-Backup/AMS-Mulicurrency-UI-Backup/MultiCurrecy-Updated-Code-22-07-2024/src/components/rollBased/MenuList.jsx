import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import Swal from "sweetalert2";
import CustomAlert from "../../layout/CustomAlert";
import swal from "sweetalert";
// import Select from "react-select";

export default function MenuList() {
  const UserId = localStorage.getItem("userName");
  const [rollData, setrollData] = useState([]);
  const [strRoleName, setStrRoleName] = useState("");
  const [menulist, setMenuList] = useState("");
  const [error, setError] = useState("");
  const [strParticipantName, setStrParticipantName] = useState("");
  const [participantData, setParticipantData] = useState([]);

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

  // ROLE NAME

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

  // MENU LIST
  const [menuData, setmenuData] = useState([]);
  useEffect(() => {
    menuList();
  }, []);

  const menuList = async () => {
    try {
      const response = await amsApi.post(`menuMaster/getallmenulist`, {});
      if (response.data.code === "S0000") {
        setmenuData(response.data.menuMasters);
      } else {
      }
    } catch (error) {}
  };

  // ON SUBMIT MENU
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (menulist === "" || strRoleName === "") {
      setError(true);
    } else if (!UserId) {
      swal("Please check your userid ");
    } else {
      try {
        const response = await amsApi.post(
          `/useraccessmenu/saveuseraccesmenu`,
          {
            strUserId: UserId,
            strMenuId: menulist,
            strMenuRoleId: strRoleName,
          }
        );
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

  const handleClick = () => {
    setStrRoleName("");
    setMenuList("");
    setStrParticipantName("");
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-1 ">
          <div className="px-4 sm:px-10 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Menu Assignment
              </p>
            </div>
          </div>
        </div>

        <div className=" md:col-span-2 lg:col-span-1  bg-white">
          <form>
            <div className="">
              <div className="px-4 sm:p-3">
                <div className="grid md:grid-cols-2 lg:grid-cols-3 md:gap-6 px-4">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Participant Name
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="strParticipantName"
                      name="strParticipantName"
                      value={strParticipantName}
                      onChange={(e) => {
                        setStrParticipantName(e.target.value);
                        rollId(e.target.value);
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {participantData.map((data) => (
                        <option
                          className="capatlize text-lg"
                          value={data.strParticipantName}
                        >
                          {data.strParticipantName || ""}
                        </option>
                      ))}
                    </select>
                    {error && strParticipantName.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter ParticipantId
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Role Name
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="strRoleName"
                      name="strRoleName"
                      value={strRoleName}
                      onChange={(e) => setStrRoleName(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option> Select </option>
                      {rollData.map((data) => (
                        <option
                          className="capatlize text-lg"
                          value={data.strRoleId}
                        >
                          {data.strRoleName}
                        </option>
                      ))}
                    </select>
                    {error && strRoleName.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter RoleId!
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="">
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Menu List <span className="text-red-600 px-2">*</span>
                    </label>

                    <select
                      id="menulist"
                      name="menulist"
                      value={menulist}
                      onChange={(e) => setMenuList(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {menuData.map((data) => (
                        <option
                          className="capatlize text-sm"
                          value={
                            data.strMenuId +
                            "~sep~" +
                            data.strParentMenuId +
                            "~sep~" +
                            data.strMenuName
                          }
                        >
                          {data.strMenuName}
                        </option>
                      ))}
                    </select>
                    {error && menulist.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select menulist
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
                onClick={handleClick}
                type="danger"
                className="bg-red-500 hover:bg-red-700 focus:ring-2 focus:ring-red-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-red-700 rounded"
              >
                Clear
              </button>
            </div>
          </form>
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
    </AppLayout>
  );
}

import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import amsApi from "../../api/amsApi";
import AppLayout from "../../layout/AppLayout";
import CustomAlert from "../../layout/CustomAlert";
const AddRole = () => {
  const { authInfo } = useSelector((state) => state.auth);
  const UserId = authInfo.userId;
  const [strParticipantId, setStrParticipantId] = useState("");
  const [participantData, setParticipantData] = useState([]);
  const [description, setdescription] = useState("");
  const [roleName, setRolename] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

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

  const adddetails = async (event) => {
    event.preventDefault();
    if (roleName === "" || description === "" || strParticipantId === "") {
      setError(true);
    } else {
      try {
        setLoading(true);
        const response = await amsApi.post(
          `/roleMaster/addRole`,
          {
            strParticipantId: strParticipantId,
            strRoleName: roleName,
            strDescription: description,
          },
          {
            headers: {
              "content-type": "application/json",
              Authorization: `Bearer ${authInfo.access_token}`,
            },
          }
        );
        if (response.status === 200) {
          setLoading(false);
          handleShowSuccess(response.data.message);
          setError("");
          handleClick();
        } else {
          setLoading(false);
          handleShowError(response.data.message);
        }
      } catch (error) {
        setLoading(false);
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };

  const handleClick = () => {
    setStrParticipantId("");
    setRolename("");
    setdescription("");
  };

  return (
    <>
      <AppLayout>
        <div className="max-w-7xl mx-auto h-full">
          <div className="w-full shadow-md mt-2 ">
            <div className="px-4 sm:px-10 bg-blue-200 ">
              <div className=" flex  items-center justify-between">
                <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                  Add Role
                </p>
              </div>
            </div>
          </div>
          <form>
            <div className="sm:overflow-hidden bg-white ">
              <div className="px-4 sm:p-3">
                <div className="grid md:grid-cols-3 md:gap-6">
                  <div>
                    <label
                      htmlFor="GL Account Type"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Participant Name
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="strParticipantId"
                      name="strParticipantId"
                      value={strParticipantId}
                      onChange={(e) => setStrParticipantId(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {participantData.map((data) => (
                        <option
                          className="capatlize text-lg"
                          value={data.strParticipantName}
                        >
                          {data.strParticipantName || "-"}
                        </option>
                      ))}
                    </select>
                    {error && strParticipantId.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Participant Name
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="GL Account Type"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Role Name
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      id="roleName"
                      name="roleName"
                      value={roleName}
                      onChange={(e) => setRolename(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Role Name"
                    />
                    {error && roleName.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter RoleId!
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="GL Account Type"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Description
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="description"
                      name="description"
                      value={description}
                      onChange={(e) => setdescription(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Description"
                    />
                    {error && description.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter description
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
                onClick={adddetails}
                className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white text-sm   py-1 px-2 border border-blue-700 rounded"
              >
                {loading ? "Loading..." : "Submit"}
              </button>
              <button
                title="Clear Data"
                onClick={handleClick}
                type="danger"
                className="bg-red-500 hover:bg-red-700 focus:ring-2 focus:ring-red-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-red-700 rounded"
              >
                clear
              </button>
            </div>
          </form>
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
    </>
  );
};

export default AddRole;

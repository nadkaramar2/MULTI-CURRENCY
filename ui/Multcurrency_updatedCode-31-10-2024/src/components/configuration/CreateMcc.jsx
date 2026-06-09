import React, { useState } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
export default function CreateMcc() {
  const [strMccCode, setstrMccCode] = useState("");
  const [strMccCodeDesc, setstrMccCodeDesc] = useState("");
  const [error, setError] = useState("");
  let participantID = sessionStorage.getItem("Participantid");
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

  // save form
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (strMccCode === "" || strMccCode.length === 0 || strMccCodeDesc === "") {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(`mcc_code/addMcc`, {
          strParticipantID: participantID,
          strMccCode: strMccCode,
          strMccCodeDesc: strMccCodeDesc,
        });
        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          setError("");
          handleClick();
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };

  const handleClick = () => {
    setstrMccCode("");
    setstrMccCodeDesc("");
  };
  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Add MCC Code
              </p>
            </div>
          </div>
        </div>
        <div className="md:col-span-2 lg:col-span-1 ">
          <form className="">
            <div className="overflow-hidden shadow ">
              <div className=" px-4  sm:p-2 bg-white  ">
                <div className="grid gap-2 px-8  md:grid-cols-1 ">
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-8 py-1 text-base font-normal text-gray-700 bg-white  "
                    >
                      MCC Code<span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="strMccCode"
                      value={strMccCode}
                      onChange={(e) => setstrMccCode(e.target.value)}
                      className="block w-60 px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Mcc Code"
                      autoComplete="off"
                    />
                    {error && strMccCode.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Mcc Code
                      </p>
                    ) : (
                      ""
                    )}
                  </div>

                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-2 py-1 mr-1 text-base font-normal text-gray-700 bg-white  "
                      // className="block text-xs font-semibold text-gray-700"
                    >
                      MCC Description
                      <span className="text-red-600 px-1">*</span>
                    </label>
                    <input
                      type="text"
                      id="strMccCodeDesc"
                      disabled={!strMccCode}
                      value={strMccCodeDesc}
                      onChange={(e) => setstrMccCodeDesc(e.target.value)}
                      className="block w-60 px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Mcc Description"
                      autoComplete="off"
                    />
                    {error && strMccCodeDesc.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Mcc Description
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                </div>
              </div>
              <div className="bg-gray-100 px-4 py-1 text-right sm:px-6 ">
                <button
                  title="Click Submit Button"
                  type="button"
                  data-modal-toggle="defaultModal"
                  onClick={handleSubmit}
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-3  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1.5 px-3 mx-4 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                >
                  Clear
                </button>
              </div>
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

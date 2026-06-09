import React, { useState } from "react";
import AppLayout from "../../layout/AppLayout";
import swal from "sweetalert";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
export default function ResponseCodeDescrition() {
  const [ResponseCode, setresponsecode] = useState("");
  const [ResponseDescription, setresponseDescription] = useState("");
  let participantID = sessionStorage.getItem("Participantid");
  let useid = localStorage.getItem("userName");
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

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      ResponseCode === "" ||
      ResponseCode.length === 0 ||
      ResponseDescription === "" ||
      ResponseDescription.length === 0
    ) {
      setError(true);
    } else if (!participantID) {
      swal("Please check your participantID ");
    } else {
      try {
        const response = await amsApi.post(
          `ResponseCodeDescrption/addResponseCode`,
          {
            strRespCode: ResponseCode,
            strRespDescr: ResponseDescription,
            strCreatedBy: useid,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          setError("");
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };
  // clear input data
  const handleClick = () => {
    setresponsecode("");
    setresponseDescription("");
  };
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Create Response Code
              </p>
            </div>
          </div>
        </div>
        <div className="bg-white sm:rounded-md">
          <form className="" onSubmit={handleSubmit}>
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className=" px-4 py-1 sm:p-2 bg-white  ">
                <div className="grid gap-2  grid-cols-1 px-5 ">
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-11 py-1 text-base font-normal text-gray-700 bg-white  "
                    >
                      Response Code{""}
                      <span className="text-red-600  ">*</span>
                    </label>
                    <input
                      type="text"
                      id="DefineResponseCode"
                      value={ResponseCode}
                      onChange={(e) =>
                        setresponsecode(e.target.value.toUpperCase())
                      }
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Response Code"
                      autoComplete="off"
                    />
                    {error && ResponseCode.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please enter ResponseCode
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-2 py-1 text-base font-normal text-gray-700 bg-white  "
                    >
                      Response Description{""}
                      <span className="text-red-600 px-1">*</span>
                    </label>
                    <input
                      type="text"
                      id="ResponseDescription"
                      value={ResponseDescription}
                      onChange={(e) => setresponseDescription(e.target.value)}
                      disabled={!ResponseCode}
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Response Description"
                      autoComplete="off"
                    />
                    {error && ResponseDescription.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please enter ResponseDescription
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                </div>
              </div>
              <div className="bg-gray-100 px-4 py-1 text-right sm:px-2 ">
                <button
                  title="Click Submit Button"
                  type="submit"
                  // disabled={!creditcategory}
                  data-modal-toggle="defaultModal"
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-2 mx-2 my-2  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1.5 px-4 mx-2 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                >
                  Clear
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
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

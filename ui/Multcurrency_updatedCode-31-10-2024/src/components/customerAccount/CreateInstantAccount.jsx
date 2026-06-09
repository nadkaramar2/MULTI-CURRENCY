import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import Swal from "sweetalert2";
import { v4 as uuidv4 } from "uuid";
import CustomAlert from "../../layout/CustomAlert";
export default function CreateInstantAccount() {
  const [alldata, setAlldata] = useState([]);
  const [strParticipantId, setstrParticipantId] = useState("");
  const [strQuantity, setstrQuantity] = useState("");
  const [strAccountName, setstrAccountName] = useState("");
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
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

  useEffect(() => {
    gLAccountCreationlist();
  }, [strParticipantId]);

  const gLAccountCreationlist = async () => {
    try {
      const response = await amsApi.post(
        `accountType/getACTypeListByParticiptWise`,
        {
          strParticipantId: participantID,
        }
      );
      if (response.data.code === "S0000") {
        setAlldata(response.data.accountTypeMasterlistData);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };
  // save form
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      strParticipantId === "" ||
      strParticipantId.length === 0 ||
      strQuantity === "" ||
      strAccountName === "  "
    ) {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(
          `instanceAccount/createInstantAcount`,
          {
            strAccountType: strParticipantId,
            strQuantity: strQuantity,
            strAccountName: strAccountName,
          }
        );

        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          setError("");
          setstrParticipantId("");
          setstrQuantity("");
          setstrAccountName("");
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };

  const handleClick = () => {
    setstrParticipantId("");
    setstrQuantity("");
    setstrAccountName("");
  };
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Instant Account Generation Form
              </p>
            </div>
          </div>
        </div>
        <div className="md:col-span-2 lg:col-span-1 sm:rounded-md ">
          <form className="">
            <div className="overflow-hidden shadow ">
              <div className=" px-4 sm:p-2 bg-white  ">
                <div className="grid gap-6 px-8 md:grid-cols-3 ">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Account Type <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      name="strParticipantId"
                      id="strParticipantId"
                      value={strParticipantId}
                      onChange={(e) => setstrParticipantId(e.target.value)}
                      className="block w-full px-3 py-1 text-base font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option>Select</option>
                      {alldata.map((data) => (
                        <option
                          className="capatlize text-sm"
                          value={data.strAccountType}
                        >
                          {data.strAccountType} -{""}
                          {data.strDescription || ""}
                        </option>
                      ))}
                    </select>
                    {error && strParticipantId.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Account Type
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
                      Quantity <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="strQuantity"
                      value={strQuantity}
                      // onChange={(e) => setstrQuantity(e.target.value)}
                      onChange={(e) => {
                        const value = e.target.value;
                        setstrQuantity(value);

                        const regex = /^[0-9]*$/;
                        if (!regex.test(value)) {
                          setErrorMessage(
                            " Only allow numeric digits"
                            // "Please enter only numeric values"
                          );
                        } else {
                          setErrorMessage("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (strQuantity.length === 0) {
                          setError("  Please Enter Quantity!");
                          setErrorMessage("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-base font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Quantity"
                    />
                    {errorMessage ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage}
                      </p>
                    ) : error && strQuantity.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please Enter Quantity!
                      </p>
                    ) : null}
                    {/* {error && strQuantity.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Enter Quantity
                        </p>
                      ) : (
                        ""
                      )} */}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Instant Account Name
                    </label>
                    <input
                      type="text"
                      id="strAccountName"
                      value={strAccountName}
                      onChange={(e) => setstrAccountName(e.target.value)}
                      className="block w-full px-3 py-1 text-base font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Account Number"
                    />
                    {error && strAccountName.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Account Name
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
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-2 px-5  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-2 px-5 mx-4 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
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

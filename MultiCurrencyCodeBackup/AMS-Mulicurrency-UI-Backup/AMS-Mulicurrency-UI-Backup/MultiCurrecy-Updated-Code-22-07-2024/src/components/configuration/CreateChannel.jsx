import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
export default function CreateChannel() {
  const [alldata2, setalldata2] = useState([]);
  const [strChannelType, setstrChannelType] = useState("");
  const [strChannelDescription, setstrChannelDescription] = useState("");
  const [channelCode, setchannelCode] = useState("");
  const [error, setError] = useState("");
  let participantID = sessionStorage.getItem("Participantid");
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [glAccountType, setglAccountType] = useState("");
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

  useEffect(() => {
    gLAccountCreationlist();
  }, []);

  const gLAccountCreationlist = async () => {
    try {
      const response = await amsApi.get(`tcsTypeMaster/getGlType`);

      if (response.data.code === "S0000") {
        setalldata2(response.data.listData);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };
  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  // save form
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      strChannelType === "" ||
      strChannelType.length === 0 ||
      glAccountType === "" ||
      // strChannelDescription === "" ||
      channelCode === ""
    ) {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(`channels/addChannel`, {
          strParticipantId: participantID,

          strChannelType: strChannelType,
          // strChannelDescription: strChannelDescription,
          glAccountType: glAccountType,
          channelCode: channelCode,
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
    setglAccountType("");
    setstrChannelType("");
    setstrChannelDescription("");
    channelCode("");
  };
  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Add Channel
              </p>
            </div>
          </div>
        </div>
        <div className="md:col-span-2 lg:col-span-1 sm:rounded-md bg-white">
          <form className="">
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className=" px-4 py-2 sm:p-2 bg-white  ">
                <div className="grid gap-2  md:grid-cols-1 px-4 ">
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-12 py-1 text-base font-normal text-gray-700 bg-white"
                    >
                      Channel Type <span className="text-red-600 px-1">*</span>
                    </label>
                    <input
                      type="text"
                      id="strChannelType"
                      value={strChannelType}
                      onChange={(e) => setstrChannelType(e.target.value)}
                      className="block w-60 px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Channel Type"
                    />
                    {error && strChannelType.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Channel Type
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-1   py-1  text-base font-normal text-gray-700 bg-white"
                      // className="block text-xs font-semibold text-gray-700"
                    >
                      Channel Description
                      <span className="text-red-600 px-1">*</span>
                    </label>
                    <input
                      type="text"
                      id="strChannelDescription"
                      disabled={!strChannelType}
                      value={strChannelDescription}
                      onChange={(e) => setstrChannelDescription(e.target.value)}
                      className="block w-60 px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Channel Description"
                      autoComplete="off"
                    />
                    {error && strChannelDescription.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Channel Description
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-10 py-1 text-base font-normal text-gray-700 bg-white"
                      //  className="block text-xs font-semibold text-gray-700"
                    >
                      Channel Code
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      maxLength={3}
                      minLength={3}
                      id="channelCode"
                      disabled={!strChannelDescription}
                      value={channelCode}
                      onChange={(e) => setchannelCode(e.target.value)}
                      className="block w-60 px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Channel Code"
                      autoComplete="off"
                    />
                    {error && channelCode.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Channel Code
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 mr-1 text-base font-normal text-gray-700 bg-white"
                    >
                      GL Account Type{""}
                      <span className="text-red-600 px-3 ml-2">*</span>
                    </label>
                    <select
                      name="glAccountType"
                      id="glAccountType"
                      disabled={!channelCode}
                      value={glAccountType}
                      onChange={(e) => setglAccountType(e.target.value)}
                      className="block w-60 px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>

                      {alldata2.map((data) => (
                        <option className="capatlize text-md] max-w-[20rem]">
                          {data.strGLAccountType} {""}
                          {data.strGLAccountNumber || ""}
                        </option>
                      ))}
                    </select>
                    {/* {error && strGLAccountType.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Select GL Account Type!
                        </p>
                      ) : (
                        ""
                      )} */}
                  </div>
                </div>
              </div>
              <div className="bg-gray-100 px-4 py-1 text-right sm:px-6 ">
                <button
                  type="button"
                  data-modal-toggle="defaultModal"
                  onClick={handleSubmit}
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-3  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
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

import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
import swal from "sweetalert";
export default function GLAccountLoadblc() {
  const userId = localStorage.getItem("userName");
  const [alldata, setalldata] = useState([]);
  const [chanelldata1, setchanelldata1] = useState([]);
  const [strParticipantId, setstrParticipantId] = useState("");
  const [channel, setchannel] = useState("");
  const [channelCode, setChannelCode] = useState("");
  const [loadblc, setLoadblc] = useState("");
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

  const handleClick = () => {
    setstrParticipantId("");
    setchannel("");
    setLoadblc("");
    setTotal("");
  };
  useEffect(() => {
    glAccountLoadblc();
  }, [strParticipantId]);

  const glAccountLoadblc = async () => {
    try {
      const response = await amsApi.get(
        `gl-account-type/getThirdPartyAllowGLAcountTypeList`,
        {}
      );

      if (response.data.code === "S0000") {
        setalldata(response.data.glAccountviewModels);
        // swal(response.data.message);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(
        "Sorry, the server is under mentaines. Please try again later"
      );
    }
  };
  useEffect(() => {
    channelList();
  }, [channel]);
  const channelList = async () => {
    try {
      const response = await amsApi.post(
        `channels/getChannelList`,

        {}
      );

      if (response.data.code === "S0000") {
        setchanelldata1(response.data.channelList);
        // swal(response.data.message);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(
        "Sorry, the server is under mentaines. Please try again later"
      );
    }
  };

  // Get Account Holder Name
  const [blcdata, setBlcdata] = useState([]);
  const balance = blcdata.map((t) => [t.strClosingBalance]);
  sessionStorage.setItem("Balance", balance);
  let balance1 = sessionStorage.getItem("Balance");
  const AccountNumber = blcdata.map((t) => [t.strAccountNumber]);
  sessionStorage.setItem("strAccountNumber", AccountNumber);
  let sAccountNumber = sessionStorage.getItem("strAccountNumber");
  useEffect(() => {
    Balance();
    setError("");
  }, [strParticipantId]);
  const Balance = async () => {
    // alert("please enter a valid Input (non-empty values).");
    try {
      const response = await amsApi.post(`gl-account-type/getGLDescrption`, {
        strGLAccountType: strParticipantId.split("-")[0],
      });
      if (response.data.code === "S0000") {
        setBlcdata(response.data.glAccountviewModels);
      } else {
      }
    } catch (error) {}
  };

  // total balance
  const [total, setTotal] = useState("");
  const calculateTotal = () => {
    var totalBalance = parseFloat(balance1) + parseFloat(loadblc);
    setTotal(totalBalance);
  };
  // save the Data
  // save info
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      channel === "" ||
      channel.length === 0 ||
      loadblc === "" ||
      strParticipantId === ""
    ) {
      setError(true);
    } else if (!sAccountNumber) {
      swal("Please check your Account Number ");
    } else {
      try {
        const response = await amsApi.post(
          `gl-account-load/addGLAccountLoadMaster`,
          {
            strGLAccountType: strParticipantId.split("-")[0],
            strAccountNumber: sAccountNumber,
            strChannel: channel + "-" + channelCode,
            strLoadedBalance: loadblc,
            strGLAccountDescription: strParticipantId.split("-")[1],
            strAccountCreatedBy: userId,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code === "S0000") {
          // setList(response.data.accountLoadMaster);
          handleShowSuccess(response.data.message);
          setError("");
          setstrParticipantId("");
          setchannel("");
          setLoadblc("");
          setBlcdata([]);
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
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                GL Account Loading
              </p>
            </div>
          </div>

          <div className="mt-1 md:col-span-2 lg:col-span-1">
            <form className="" onSubmit={handleSubmit}>
              <div className="overflow-hidden shadow ">
                <div className=" px-4 py-2 sm:p-2 bg-white  ">
                  <div className="grid gap-4  md:grid-cols-3 px-4">
                    <div>
                      <label
                        htmlFor="GL Account Type"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Account Type<span className="text-red-600 px-2">*</span>
                      </label>
                      <select
                        name="strParticipantId"
                        id="strParticipantId"
                        value={strParticipantId}
                        onChange={(e) => setstrParticipantId(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      >
                        <option> Select </option>
                        {alldata.map((data) => (
                          <option
                            className="capatlize text-lg"
                            value={
                              data.strGLAccountType +
                                "-" +
                                data.strGLAccountDescription || ""
                            }
                          >
                            {data.strGLAccountType}-
                            {data.strGLAccountDescription || ""}
                          </option>
                        ))}
                      </select>
                      {error && strParticipantId.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please select Account Type
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                  </div>
                  <div className="grid gap-4  mb-2  mt-2 md:grid-cols-3 px-4">
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        GL Account Number
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <input
                        type="text"
                        // onClick={Balance}
                        defaultValue={sAccountNumber}
                        autoComplete="off"
                        placeholder="Enter GL Account Number"
                        className=" block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      />
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Available Balance
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <input
                        type="text"
                        id="phone"
                        onClick={Balance}
                        disabled
                        defaultValue={balance1}
                        placeholder="Available Balance"
                        className=" block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                        autoComplete="off"
                      />
                    </div>
                  </div>
                  <div className="grid gap-4  mb-2  mt-2 md:grid-cols-3 px-4">
                    <div>
                      <label
                        htmlFor="GL Account Description"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Load Channel<span className="text-red-600 px-2">*</span>
                      </label>
                      <select
                        id="setchannel"
                        name="setchannel"
                        value={channel}
                        onChange={(e) => {
                          for (let i = 0; i < chanelldata1.length; i++) {
                            if (
                              chanelldata1[i].strChannelType === e.target.value
                            ) {
                              setChannelCode(chanelldata1[i].channelCode);
                            }
                          }
                          setchannel(e.target.value);
                        }}
                        className=" block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      >
                        <option>Select </option>
                        {chanelldata1.map((data) => (
                          <option
                            className="captlize text-lg"
                            value={data.strChannelType}
                          >
                            {/* {data.strChannelType} */}
                            {data.strChannelType || ""} -{""}
                            {data.strChannelDescription || ""}
                          </option>
                        ))}
                      </select>
                      {error && channel.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please select channel
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                  </div>
                  <div className="grid gap-4  mb-2  mt-2 md:grid-cols-3 px-4">
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Load Balance<span className="text-red-600 px-2">*</span>
                      </label>
                      <input
                        type="text"
                        id="Grace Period In Days*"
                        value={loadblc}
                        // onChange={(e) => setLoadblc(e.target.value)}
                        onChange={(e) => {
                          const value = e.target.value;
                          setLoadblc(value);

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
                        onKeyUp={calculateTotal}
                        onBlur={() => {
                          if (loadblc.length === 0) {
                            setError("Please Enter LoadBalance!");
                            setErrorMessage("");
                          }
                        }}
                        className=" block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                        placeholder="Enter Load Balance"
                      />
                      {errorMessage ? (
                        <p className="text-red-500 text-xs font-medium">
                          {errorMessage}
                        </p>
                      ) : error && loadblc.length <= 0 ? (
                        <p className="text-red-500 text-xs font-medium">
                          Please Enter LoadBalance!
                        </p>
                      ) : null}
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Total Balance
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <div>
                        <input
                          type="text"
                          defaultValue={total}
                          disabled
                          className="bg-gray-100 block w-full px-3 py-1 text-sm font-normal text-gray-700  bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                          placeholder="Total Balance"
                        />
                      </div>
                    </div>
                  </div>
                </div>
                <div className="bg-gray-100 px-4 py-1.5 text-right sm:px-2 ">
                  <button
                    title="Click Submit Button"
                    type="submit"
                    data-modal-toggle="defaultModal"
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
            </form>
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
    </AppLayout>
  );
}

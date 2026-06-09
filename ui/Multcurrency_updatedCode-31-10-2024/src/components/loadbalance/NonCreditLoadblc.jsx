import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
import swal from "sweetalert";
export default function NonCreditLoadblc() {
  const [accounttype, setAccountType] = useState("");
  const [type, setType] = useState([]);
  const [channel, setChannel] = useState("");
  const [channelCode, setChannelCode] = useState("");
  const [loadblc, setLoadblc] = useState("");
  const [channeldata, setChanneldata] = useState([]);
  const [accountno, setAccountno] = useState("");
  const [alldata, setAlldata] = useState([]);
  const holdername = alldata.map((t) => [t.strAccountHolderName]);
  sessionStorage.setItem("Holdername", holdername);
  let Holdername1 = sessionStorage.getItem("Holdername");
  const balance = alldata.map((t) => [t.strClosingBalance]);
  sessionStorage.setItem("Balance", balance);
  let balance1 = sessionStorage.getItem("Balance");
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
  // Dropdown Account Type
  useEffect(() => {
    Accounttype();
  }, []);

  const Accounttype = async () => {
    try {
      const response = await amsApi.post(
        `accountType/getNonCrditAccounType`,
        {
          participantID,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setType(response.data.accountTypeMasterlistData);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };

  // Get Account Holder Name
  const Holdername = async () => {
    if (
      accountno === "" ||
      accountno.length === 0 ||
      accounttype === "" ||
      accounttype.length === 0
    ) {
      // setError(true);
      //  swal("please enter a valid Input (non-empty values).");
    } else {
      try {
        const response = await amsApi.post(`account/getAccountBalancAndName`, {
          strAccountNumber: accountno,
          strAccountType: accounttype,
        });
        if (response.data.code === "S0000") {
          setAlldata(response.data.accountInfoList);
        } else {
          // showError(response.data.message);
        }
      } catch (error) {
        // showError(error.response.data.message);
      }
    }
  };

  // Load Channel
  useEffect(() => {
    Channel();
  }, []);

  const Channel = async () => {
    try {
      const response = await amsApi.post(
        `channels/getChannelList`,
        {},
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setChanneldata(response.data.channelList);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error);
    }
  };

  const handleClick = () => {
    setAccountType("");
    setAccountno("");
    setChannel("");
    setLoadblc("");
    setTotal("");
    setAlldata([]);
  };
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [errorMessage1, setErrorMessage1] = useState("");
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      channel === "" ||
      channel.length === 0 ||
      loadblc === "" ||
      accounttype === "" ||
      accountno === ""
    ) {
      setError(true);
    } else if (!participantID) {
      swal("Please check your participantID ");
    } else {
      try {
        const response = await amsApi.post(
          `transactionHandler/processToLoadBalance1`,
          {
            strParticipantId: participantID,
            strAccountNumber: accountno,
            strAccountType: accounttype,
            strChannel: channel + "-" + channelCode,
            strLoadedBalance: loadblc,
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
          handleClick();
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {}
    }
  };

  const [total, setTotal] = useState("");
  const calculateTotal = () => {
    var totalBalance = parseFloat(balance1) + parseFloat(loadblc);
    setTotal(totalBalance);
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Load Account Balance
              </p>
            </div>
          </div>
          <div className=" mt-1 md:col-span-2 lg:col-span-1 ">
            <form className="" onSubmit={handleSubmit}>
              <div className="overflow-hidden shadow sm:rounded-md">
                <div className=" px-4 py-2 sm:p-2 bg-white ">
                  <div className="grid gap-4  md:grid-cols-2 lg:grid-cols-4  px-8">
                    <div>
                      <label
                        htmlFor="GL Account Type"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Account Type<span className="text-red-600 px-2">*</span>
                      </label>
                      <select
                        id="accountType"
                        name="accountType"
                        value={accounttype}
                        onChange={(e) => setAccountType(e.target.value)}
                        className=" block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option>Select</option>
                        {type.map((data) => (
                          <option value={data.strAccountType}>
                            {data.strAccountType || "-"} -{data.strDescription}
                            {""}
                          </option>
                        ))}
                      </select>
                      {error && accounttype.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please select Account Type!
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                  </div>

                  <div className="grid gap-4  md:grid-cols-2 lg:grid-cols-3  px-8">
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Account Number
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <input
                        type="text"
                        disabled={!accounttype}
                        value={accountno}
                        // onChange={(e) => setAccountno(e.target.value)}
                        onChange={(e) => {
                          const value = e.target.value;
                          setAccountno(value);

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
                        onKeyUp={Holdername}
                        onBlur={() => {
                          if (accountno.length === 0) {
                            setError("Please Enter Account Number!");
                            setErrorMessage("");
                          }
                        }}
                        className=" block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Account Number"
                        autoComplete="off"
                      />
                      {errorMessage ? (
                        <p className="text-red-500 text-xs font-medium">
                          {errorMessage}
                        </p>
                      ) : error && accountno.length <= 0 ? (
                        <p className="text-red-500 text-xs font-medium">
                          Please Enter Account Number!
                        </p>
                      ) : null}
                    </div>
                    <div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Account Holder Name
                          <span className="text-red-600 px-2">*</span>
                        </label>
                        <input
                          type="text"
                          autoComplete="off"
                          // onClick={Holdername}
                          defaultValue={Holdername1}
                          placeholder="Account Holder Name"
                          className=" bg-gray-100 block w-full px-3 py-1 text-sm font-normal text-gray-700  bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                    </div>
                    <div>
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
                          defaultValue={balance1}
                          // onClick={Holdername}
                          autoComplete="off"
                          placeholder="Available Balance"
                          className="bg-gray-100  block w-full px-3 py-1 text-sm font-normal text-gray-700  bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                    </div>
                    <div>
                      <label
                        htmlFor="GL Account Description"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Load Channel<span className="text-red-600 px-2">*</span>
                      </label>
                      <select
                        id="accountType"
                        name="accountType"
                        disabled={!accountno}
                        value={channel}
                        onChange={(e) => {
                          for (let i = 0; i < channeldata.length; i++) {
                            if (
                              channeldata[i].strChannelType === e.target.value
                            ) {
                              setChannelCode(channeldata[i].channelCode);
                            }
                          }
                          setChannel(e.target.value);
                        }}
                        className=" block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option>Select</option>
                        {channeldata.map((data) => (
                          <option value={data.strChannelType}>
                            {data.strChannelType || "-"} -
                            {data.strChannelDescription}
                            {""}
                          </option>
                        ))}
                      </select>
                      {error && channel.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Select Channel!
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
                        Load Balance<span className="text-red-600 px-2">*</span>
                      </label>
                      <input
                        type="text"
                        id="balance "
                        disabled={!channel}
                        value={loadblc}
                        // onChange={(e) => setLoadblc(e.target.value)}
                        onChange={(e) => {
                          const value = e.target.value;
                          setLoadblc(value);

                          const regex = /^[0-9]*$/;
                          if (!regex.test(value)) {
                            setErrorMessage1(
                              " Only allow numeric digits"
                              // "Please enter only numeric values"
                            );
                          } else {
                            setErrorMessage1("");
                          }
                          setError("");
                        }}
                        onKeyUp={calculateTotal}
                        onBlur={() => {
                          if (loadblc.length === 0) {
                            setError("Please Enter LoadBalance!");
                            setErrorMessage1("");
                          }
                        }}
                        className=" block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Load Balance"
                        autoComplete="off"
                      />
                      {errorMessage1 ? (
                        <p className="text-red-500 text-xs font-medium">
                          {errorMessage1}
                        </p>
                      ) : error && loadblc.length <= 0 ? (
                        <p className="text-red-500 text-xs font-medium">
                          Please Enter LoadBalance!
                        </p>
                      ) : null}
                      {/* {error && loadblc.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Enter LoadBalance!
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
                        Total Balance
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <div>
                        <input
                          type="text"
                          // onClick={calculateTotal}
                          defaultValue={total}
                          autoComplete="off"
                          placeholder="Total Balance"
                          className="bg-gray-100  block w-full px-3 py-1 text-sm font-normal text-gray-700  bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
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

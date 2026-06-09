import React, { useState, useEffect, useRef } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import { useReactToPrint } from "react-to-print";
import { XMarkIcon } from "@heroicons/react/24/solid";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import CustomAlert from "../../layout/CustomAlert";
export default function ControlAccountTally() {
  const [showScreen, setShowScreen] = useState({
    searchDetails: true,
    viewOptions: false,
    viewDetails: false,
  });
  const [startDate, setStartDate] = useState("");
  const [todayDate, setTodayDate] = useState("");
  const [prevStatus, setPrevStatus] = useState("");
  const [error, setError] = useState("");
  useEffect(() => {
    let currDate = new Date().toLocaleDateString("en-CA");
    setTodayDate(currDate);
  }, []);
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

  const handleSubmitPrevStatus = async (event) => {
    event.preventDefault();
    if (prevStatus === "" || prevStatus.length === 0) {
      setError(true);
      //  swal("please enter a valid Input (non-empty values).");
    } else {
      controlAccountPrevStatusRequest();
      //  alert(prevStatus);
    }
  };
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (startDate === "" || startDate.length === 0) {
      setError(true);
      //  swal("please enter a valid Input (non-empty values).");
    } else {
      //viewSummery();
      setShowScreen((prevState) => ({ ...prevState, viewOptions: true }));
    }
  };

  const viewDetails = () => {
    controlAccountRequest();
  };

  const clearInputData = () => {
    setStartDate("");
    setPrevStatus("");
    setShowScreen((prevState) => ({ ...prevState, viewOptions: false }));
    setShowScreen((prevState) => ({ ...prevState, viewDetails: false }));
  };

  const controlAccDataList = [
    {
      label: "PPD",
      bal: "2,000",
    },
    {
      label: "AGT",
      bal: "2,500",
    },
  ];
  const [controlAccountResData, setControlAccountResData] = useState({});
  const [controlAccountArrList, setControlAccountArrList] = useState([]);

  const controlAccountRequest = async () => {
    poolAccountRequest();
    try {
      const response = await amsApi.post(
        `systemtally/getcontrolaccounttally`,
        {
          date: startDate,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.data.code === "S0000") {
        setControlAccountResData(response.data.tallyControlAccount);
        var arrSize =
          response.data.tallyControlAccount.accountTypeBlance.length;
        var arrSize2 =
          response.data.tallyControlAccount.glAccountTypeWiseBlance.length;

        const maxArrSize = Math.max(arrSize, arrSize2);
        if (arrSize !== 0) {
          let tempArr = [];

          for (var i = 0; i < maxArrSize; i++) {
            if (i < arrSize) {
              if (i < arrSize2) {
                tempArr.push({
                  id: i,
                  accountType:
                    response.data.tallyControlAccount.accountTypeBlance[i]
                      .accountType,
                  blance:
                    response.data.tallyControlAccount.accountTypeBlance[i]
                      .blance,
                  glAccType:
                    response.data.tallyControlAccount.glAccountTypeWiseBlance[i]
                      .accountType,
                  glBal:
                    response.data.tallyControlAccount.glAccountTypeWiseBlance[i]
                      .blance,
                });
              } else {
                tempArr.push({
                  id: i,
                  accountType:
                    response.data.tallyControlAccount.accountTypeBlance[i]
                      .accountType,
                  blance:
                    response.data.tallyControlAccount.accountTypeBlance[i]
                      .blance,
                });
              }
            } else {
              tempArr.push({
                id: i,

                glAccType:
                  response.data.tallyControlAccount.glAccountTypeWiseBlance[i]
                    .accountType,
                glBal:
                  response.data.tallyControlAccount.glAccountTypeWiseBlance[i]
                    .blance,
              });
            }
          }

          setControlAccountArrList(tempArr);
        }

        setShowScreen((prevState) => ({ ...prevState, viewDetails: true }));
        setError("");
      } else {
        clearInputData();
        setError("");
        handleShowError(response.data.message);
      }
    } catch (error) {
      clearInputData();
      handleShowError(error.response.data.message);
      setError("");
    }
  };
  const controlAccountPrevStatusRequest = async () => {
    poolAccountRequest();
    try {
      const response = await amsApi.post(
        `systemtally/getcontrolaccounttally`,
        {
          date: startDate,
          time: prevStatus,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.data.code === "S0000") {
        setControlAccountResData(response.data.tallyControlAccount);
        var arrSize =
          response.data.tallyControlAccount.accountTypeBlance.length;
        var arrSize2 =
          response.data.tallyControlAccount.glAccountTypeWiseBlance.length;

        const maxArrSize = Math.max(arrSize, arrSize2);
        if (arrSize !== 0) {
          let tempArr = [];

          for (var i = 0; i < maxArrSize; i++) {
            if (i < arrSize) {
              if (i < arrSize2) {
                tempArr.push({
                  id: i,
                  accountType:
                    response.data.tallyControlAccount.accountTypeBlance[i]
                      .accountType,
                  blance:
                    response.data.tallyControlAccount.accountTypeBlance[i]
                      .blance,
                  glAccType:
                    response.data.tallyControlAccount.glAccountTypeWiseBlance[i]
                      .accountType,
                  glBal:
                    response.data.tallyControlAccount.glAccountTypeWiseBlance[i]
                      .blance,
                });
              } else {
                tempArr.push({
                  id: i,
                  accountType:
                    response.data.tallyControlAccount.accountTypeBlance[i]
                      .accountType,
                  blance:
                    response.data.tallyControlAccount.accountTypeBlance[i]
                      .blance,
                });
              }
            } else {
              tempArr.push({
                id: i,

                glAccType:
                  response.data.tallyControlAccount.glAccountTypeWiseBlance[i]
                    .accountType,
                glBal:
                  response.data.tallyControlAccount.glAccountTypeWiseBlance[i]
                    .blance,
              });
            }
          }

          setControlAccountArrList(tempArr);
        }
        setShowScreen((prevState) => ({ ...prevState, viewDetails: true }));
      } else {
        clearInputData();
        handleShowError(response.data.message);
      }
    } catch (error) {
      clearInputData();
      handleShowError(error.response.data.message);
    }
  };

  const prevStatusList = [
    "2023-08-19 01:00:00",
    "2023-08-19 10:00:00",
    "2023-08-19 14:00:00",
    "2023-08-19 15:00:00",
  ];

  // Download pdf
  const conponentPDF = useRef();
  const generatePDF = useReactToPrint({
    content: () => conponentPDF.current,
    documentTitle: "Userdata",
    onAfterPrint: () => handleShowSuccess("Data saved in PDF"),
  });
  const [poolAccBal, setPoolAccBal] = useState("");
  const poolAccountRequest = async () => {
    try {
      const response = await amsApi.get(
        `thirdParty/NGN/middleWare/getPoolAccountInfo`
      );

      if (response.data.code === "S0000") {
        setPoolAccBal(response.data.poolAccountBalance);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full sticky top-0">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Control Account Tally
              </p>
            </div>
          </div>
        </div>
        {showScreen.searchDetails && (
          <>
            <div className=" md:col-span-2 lg:col-span-1  bg-white sticky top-0">
              <form className="" onSubmit={handleSubmit}>
                <div className="overflow-hidden shadow sm:rounded-md ">
                  <div className=" px-4 py-1 sm:p-2 bg-whiite ">
                    <div className="grid gap-2 md:grid-cols-4 px-8">
                      <div>
                        <label
                          htmlFor="GL Account Description"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Date of Generation
                          <span className="text-red-600 px-2">*</span>
                        </label>
                        <input
                          type="date"
                          id="startDate"
                          value={startDate}
                          onChange={(e) => setStartDate(e.target.value)}
                          className="block w-full px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                        {error && startDate.length <= 0 ? (
                          <p className="text-red-500   text-sm font-medium">
                            Please select Date of Generation
                          </p>
                        ) : (
                          ""
                        )}
                      </div>
                      <div className="">
                        <button
                          title="Click Search Button"
                          type="submit"
                          data-modal-toggle="defaultModal"
                          className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-2 px-5 mx-2 my-5 text-sm font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                        >
                          <MagnifyingGlassIcon className="h-2.5 w-3" />
                        </button>
                        <button
                          title="Clear Data"
                          type="button"
                          onClick={clearInputData}
                          className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-2 px-5 mx-2 my-5 text-sm font-medium text-white shadow-sm hover:bg-rose-700 focus:outline-none focus:ring-2 focus:ring-rose-500 focus:ring-offset-2"
                        >
                          <XMarkIcon className="h-2.5 w-3" />
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
            </div>
          </>
        )}
        {showScreen.viewOptions && (
          <>
            <div className=" md:col-span-2 lg:col-span-1  bg-blue-200 mt-1">
              <div className="overflow-hidden shadow sm:rounded-md ">
                <div className="bg-gray-100 px-4 py-2  sm:px-2 ">
                  <button
                    type="submit"
                    onClick={viewDetails}
                    className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-5  text-sm font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                  >
                    View Details
                  </button>
                  <button
                    type="button"
                    onClick={() => alert("Downloading....")}
                    className="inline-flex justify-center rounded-md border border-transparent  bg-green-500 py-1.5 px-5 mx-4 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                  >
                    PDF
                  </button>
                </div>
              </div>
            </div>
          </>
        )}

        <div className="max-w-7xl mx-auto h-full overflow-auto max-h-full lg:max-h-[32rem]">
          {showScreen.viewDetails && (
            <>
              {/* previous Status */}
              <div className=" md:col-span-2 lg:col-span-1  bg-blue-200 mt-1">
                <form className="" onSubmit={handleSubmitPrevStatus}>
                  <div className="overflow-hidden shadow sm:rounded-md ">
                    <div className=" px-4 py-2 sm:p-2 bg-blue-200  ">
                      <div className="grid gap-6  mt-1 md:grid-cols-3 px-8">
                        <div>
                          <label
                            htmlFor="GL Account Description"
                            className="block text-sm font-semibold text-gray-700"
                          >
                            Previous Status
                            <span className="text-red-600 px-2">*</span>
                          </label>
                          <select
                            id="strPrevStatusId"
                            name="strPrevStatusId"
                            value={prevStatus}
                            onChange={(e) => setPrevStatus(e.target.value)}
                            className="block w-full px-3 py-1 text-base font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          >
                            <option value="">Select</option>
                            {controlAccountResData.previousStatus.map(
                              (data) => (
                                <optgroup key={uuidv4()}>
                                  <option
                                    className="capatlize text-lg"
                                    value={data.time}
                                  >
                                    {data.date + "" + data.time || "-"}
                                  </option>
                                </optgroup>
                              )
                            )}
                          </select>
                          {error && prevStatus.length <= 0 ? (
                            <p className="text-red-500   text-sm font-medium">
                              Please select prevStatus
                            </p>
                          ) : (
                            ""
                          )}
                        </div>
                        <div className="self-end  sm:px-2 ">
                          <button
                            type="submit"
                            data-modal-toggle="defaultModal"
                            className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-5  text-sm font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                          >
                            Search
                          </button>
                          {/* <button
                                                            type="button"
                                                            onClick={clearInputData}
                                                            className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-2 px-5 mx-4 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                                                        >
                                                            Clear
                                                        </button> */}
                        </div>
                      </div>
                    </div>
                  </div>
                </form>
              </div>
              {/* table contol Account Tally */}

              <div className="relative overflow-x-auto shadow-md sm:rounded-lg mt-1  mb-10">
                <div ref={conponentPDF} style={{ width: "100%" }}>
                  <table className="w-full text-sm text-left text-gray-500 dark:text-gray-400">
                    <thead className="text-xs text-gray-700 uppercase dark:text-gray-400">
                      <tr>
                        <th
                          scope="row"
                          colSpan={5}
                          className="px-6 py-2   text-white   bg-blue-400 dark:bg-gray-800"
                        >
                          Report as {controlAccountResData.date}
                          {""}
                          {controlAccountResData.time}
                        </th>
                      </tr>
                      <tr>
                        <th
                          scope="col"
                          className="px-6 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          Control Account Balance
                        </th>
                        <th
                          scope="col"
                          className="px-6 py-2 bg-gray-100 text-right"
                        >
                          {controlAccountResData.controlAccountBlance}
                        </th>
                        {/* <th
                        scope="col"
                        className="px-6 py-3 bg-white text-right"
                      ></th> */}
                        <th
                          scope="col"
                          className="px-6 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          Pool Account Balance
                        </th>

                        <th
                          scope="col"
                          className="px-6 py-2 bg-gray-100 text-right"
                        >
                          {
                            //controlAccountResData.poolAccountBlance
                            poolAccBal
                          }
                        </th>
                      </tr>
                      <tr>
                        <th
                          scope="row"
                          colSpan={2}
                          className="px-6 py-2  text-center bg-blue-100 dark:bg-gray-800"
                        >
                          Account Type wise Balance
                        </th>
                        {/* <th
                        scope="row"
                        className="px-6 py-3  text-center bg-white-100 dark:bg-gray-800"
                      ></th> */}
                        <th
                          scope="row"
                          colSpan={2}
                          className="px-6 py-2  text-center bg-blue-100 dark:bg-gray-800"
                        >
                          GL Account Type wise Balance
                        </th>
                      </tr>

                      <tr>
                        <th
                          scope="col"
                          className="px-6 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          Individual Account Type
                        </th>
                        <th
                          scope="col"
                          className="px-6 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          Balance
                        </th>
                        {/* <th
                        scope="col"
                        className="px-6 py-3  bg-white-100 dark:bg-gray-800"
                      ></th> */}
                        <th
                          scope="col"
                          className="px-6 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          Individual GL Account Type
                        </th>
                        <th
                          scope="col"
                          className="px-6 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          Balance
                        </th>
                      </tr>
                    </thead>

                    <tbody>
                      <>
                        {controlAccountArrList.map((data) => (
                          <tr
                            key={uuidv4()}
                            className="border-b dark:border-neutral-500"
                          >
                            <td className="whitespace-nowrap px-6 py-2">
                              <span className="text-sm font-medium text-gray-900">
                                {data.accountType || "-"}
                              </span>
                            </td>
                            <td className="px-6 py-2 whitespace-nowrap text-right">
                              <span className="text-sm font-medium text-gray-900">
                                {data.blance || "-"}
                              </span>
                            </td>
                            {/* <td className="px-6 py-2  bg-white-100 whitespace-nowrap text-right"></td> */}
                            <td className="whitespace-nowrap px-6 py-2">
                              <span className="text-sm font-medium text-gray-900">
                                {data.glAccType || "-"}
                              </span>
                            </td>
                            <td className="px-6 py-2 whitespace-nowrap text-right">
                              <span className="text-sm font-medium text-gray-900">
                                {data.glBal || "-"}
                              </span>
                            </td>
                          </tr>
                        ))}
                      </>
                    </tbody>
                    <tbody>
                      <tr className="border-b  bg-blue-100 dark:border-neutral-500">
                        <td className="whitespace-nowrap px-6 py-2">
                          <span className="text-sm font-medium text-gray-900">
                            Total
                          </span>
                        </td>
                        <td className="px-6 py-2 whitespace-nowrap text-right">
                          <span className="text-sm font-medium text-gray-900">
                            {controlAccountResData.accountTypeBlanceTotal ||
                              "-"}
                          </span>
                        </td>
                        {/* <td className="px-6 py-2  bg-white-100 whitespace-nowrap text-right"></td> */}
                        <td className="whitespace-nowrap px-6 py-2">
                          <span className="text-sm font-medium text-gray-900">
                            Total
                          </span>
                        </td>
                        <td className="px-6 py-2 whitespace-nowrap text-right">
                          <span className="text-sm font-medium text-gray-900">
                            {controlAccountResData.glAccountTypeWiseBlanceTotal ||
                              "-"}
                          </span>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </>
          )}
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

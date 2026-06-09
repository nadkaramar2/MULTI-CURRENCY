import AppLayout from "../../layout/AppLayout";
import React, { useState, useEffect } from "react";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import Switch from "@mui/joy/Switch";
import Typography from "@mui/joy/Typography";
import {
  ArrowDownTrayIcon,
  MagnifyingGlassIcon,
} from "@heroicons/react/24/outline";
import { XMarkIcon } from "@heroicons/react/24/solid";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
export default function ViewMerchantTxnStatement() {
  const [alldata, setAlldata] = useState([]);
  const [startdate, setStartdate] = useState("");
  const [enddate, setEnddata] = useState("");
  const [cid, setCid] = useState("");
  const [accountno, setAccountno] = useState("");
  const [checked, setChecked] = useState(true);
  const [dateOpen, setDateOpen] = useState(false);
  const [duration, setDuration] = useState("");
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [checkpoint, setCheckpoint] = useState(0);
  const [searchQuery, setSearchQuery] = useState("");
  // Function to handle changes in the search input field
  const [itemPage, setItemPage] = useState(10);
  const [tcount, setTcount] = useState(1);

  // duraration
  const yesterday = new Date();
  function remainDays(leftDate) {
    yesterday.setDate(yesterday.getDate() - leftDate);
  }

  function padTo2Digits(num) {
    return num.toString().padStart(2, "0");
  }
  function formatDate(date) {
    return [
      date.getFullYear(),

      padTo2Digits(date.getMonth() + 1),

      padTo2Digits(date.getDate()),
    ].join("-");
  }

  const current = new Date();

  const date = `${current.getFullYear()}-${String(
    current.getMonth() + 1
  ).padStart(2, "0")}-${String(current.getDate()).padStart(2, "0")}`;

  const daysInCurrentMonth = `${String(current.getDate()).padStart(2, "0")}`;

  function endOfMonth(date) {
    return new Date(date.getFullYear(), date.getMonth() + 0, 0);
  }

  function startOfMonth(date) {
    return new Date(date.getFullYear(), date.getMonth() - 1, 1);
  }

  const DurationHandler = (e) => {
    setDateOpen(false);

    const selectData = e.target.value;

    setDuration(selectData);

    if (selectData === "0") {
      setStartdate(date);

      setEnddata(date);
    } else if (selectData === "1") {
      remainDays(1);

      setStartdate(formatDate(yesterday));

      setEnddata(formatDate(yesterday));
    } else if (selectData === "7") {
      remainDays(7);

      setStartdate(formatDate(yesterday));

      setEnddata(date);
    } else if (selectData === "10") {
      remainDays(10);

      setStartdate(formatDate(yesterday));

      setEnddata(date);
    } else if (selectData === "custom") {
      setDateOpen(true);
    } else if (selectData === "cMonth") {
      remainDays(parseInt(daysInCurrentMonth) - 1);

      setStartdate(formatDate(yesterday));

      setEnddata(date);
    } else if (selectData === "pMonth") {
      setStartdate(formatDate(startOfMonth(current)));

      setEnddata(formatDate(endOfMonth(current)));
    }
  };

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
    handleSubmit();
    setError("");
  }, [tcount, itemPage]);

  const handleSubmit = async () => {
    if (
      startdate === "" ||
      startdate.length === 0 ||
      enddate === "" ||
      enddate.length === 0 ||
      accountno === ""
      // cid === ""
    ) {
      setError(true);
    } else if (startdate > enddate) {
      handleShowError("please choose lesser date from To Date");
    } else {
      try {
        const response = await amsApi.post(
          `merchantTxnMaster/NGN/viewMerchantTxns`,
          {
            // cid: cid,
            accountNumber: accountno,
            fromDate: startdate,
            toDate: enddate,
            pageSize: itemPage,
            pageNumber: tcount,
          }
        );
        if (response.data.code === "S0000") {
          setAlldata(response.data.merchantTransactionCollectedList);

          // handleShowSuccess(response.data.message);
          setCheckpoint(1);
          setChecked(false);
        } else {
          handleShowError(response.data.message);
          setChecked(true);
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
      <div className="max-w-full mx-auto h-full scale-96">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className=" sm: px-11 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xm  leading-normal ">
                View Merchant Txn Statement
              </p>
              <div>
                <button className="inline-flex sm:ml-3 mt-1 sm:mt-0 items-start justify-start px-6  focus:outline-none rounded text-xs font-normal">
                  <Switch
                    checked={checked}
                    onChange={(event) => setChecked(event.target.checked)}
                    slotProps={{
                      track: {
                        children: (
                          <React.Fragment>
                            <Typography
                              component="span"
                              level="inherit"
                              sx={{ ml: "10px" }}
                            >
                              On
                            </Typography>

                            <Typography
                              component="span"
                              level="inherit"
                              sx={{ mr: "8px" }}
                            >
                              Off
                            </Typography>
                          </React.Fragment>
                        ),
                      },
                    }}
                    sx={{
                      "--Switch-thumbSize": "27px",

                      "--Switch-trackWidth": "64px",

                      "--Switch-trackHeight": "31px",
                    }}
                  />
                </button>
              </div>
            </div>
          </div>
        </div>
        {checked && (
          <div className=" md:col-span-2 lg:col-span-1  ">
            <form className="">
              <div className="overflow-hidden shadow sm:rounded-md ">
                <div className=" px-4  sm:p-2 bg-white ">
                  <div className="grid gap-4 lg:grid-cols-4 px-5">
                    {/* <div>
                      <label
                        htmlFor="text"
                        className="block  text-xs font-semibold text-gray-700 dark:text-white"
                      >
                        CID
                        <span className="text-red-600 px-2">*</span>
                      </label>

                      <input
                        type="text"
                        value={cid}
                        onChange={(e) => setCid(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                        placeholder="Enter CID"
                      />
                      {error && cid.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Enter CID
                        </p>
                      ) : (
                        ""
                      )}
                    </div> */}

                    <div>
                      <label
                        htmlFor="number"
                        className="block text-xs font-semibold text-gray-700 dark:text-white"
                      >
                        Account number
                        <span className="text-red-600 px-2">*</span>
                      </label>

                      <input
                        type="text"
                        id="text"
                        autoComplete="off"
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
                        onBlur={() => {
                          if (accountno.length === 0) {
                            setError("Please enter Account Number!");

                            setErrorMessage("");
                          }
                        }}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                        placeholder="Enter Account Number"
                      />

                      {errorMessage ? (
                        <p className="text-red-500 text-xs font-medium">
                          {errorMessage}
                        </p>
                      ) : error && accountno.length <= 0 ? (
                        <p className="text-red-500 text-xs font-medium">
                          Please enter Account Number!
                        </p>
                      ) : null}
                    </div>

                    <div>
                      <label
                        htmlFor="text"
                        className="block  text-xs font-semibold text-gray-700 dark:text-white"
                      >
                        Duration
                        <span className="text-red-600 px-2">*</span>
                      </label>

                      <select
                        id="duration"
                        name="duration"
                        value={duration}
                        onChange={(e) => DurationHandler(e)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      >
                        <option value="">Select</option>
                        <option value="0">Today</option>
                        <option value="1">Yesterday</option>
                        <option value="7">Last 7 Days</option>
                        <option value="10">Last 10 Days</option>
                        <option value="cMonth">Current Month</option>
                        <option value="pMonth">Previous Month</option>
                        <option value="custom">Custom</option>
                      </select>
                    </div>

                    {dateOpen && (
                      <>
                        <div>
                          <label
                            htmlFor="GL Account Description"
                            className="block text-xs font-semibold text-gray-700 dark:text-white"
                          >
                            Start Date
                            <span className="text-red-600 px-2">*</span>
                          </label>

                          <input
                            type="date"
                            id="account number "
                            value={startdate}
                            onChange={(e) => setStartdate(e.target.value)}
                            className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                          />
                        </div>

                        <div>
                          <label
                            htmlFor="GL Account Description"
                            className="block text-xs font-semibold text-gray-700 dark:text-white"
                          >
                            End Date
                            <span className="text-red-600 px-2">*</span>
                          </label>

                          <input
                            type="date"
                            id="account number "
                            value={enddate}
                            onChange={(e) => setEnddata(e.target.value)}
                            className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                          />
                        </div>
                      </>
                    )}

                    <div className="my-1">
                      <button
                        title="Click Search Button"
                        type="button"
                        onClick={handleSubmit}
                        data-modal-toggle="defaultModal"
                        className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800   focus:ring-offset-2 text-white font-md py-2 px-5 border border-blue-700 rounded  shadow-lg inner"
                      >
                        <MagnifyingGlassIcon className="h-3 w-3" />
                      </button>

                      <button
                        title="Clear Data"
                        type="button"
                        // onClick={handleClick}
                        className="inline-flex justify-center bg-rose-500 hover:bg-rose-700 focus:ring-2 focus:ring-rose-800 focus:ring-offset-2 py-2 px-5 mx-2 my-3 font-md  text-white  border  shadow-lg inner border-rose-700 rounded"
                      >
                        <XMarkIcon className="h-3 w-3" />
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>
        )}

        <div className="flex justify-end items-center sm:px-6 lg:px-8 bg-blue-100  ">
          <div className="flex justify-center rounded-md border border-transparent px-2 mx-2 text-sm font-medium text-white ">
            <div className="pt-2 relative mx-auto text-gray-600">
              <input
                title="Search Data"
                type="search"
                id="search"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                placeholder="Search.."
                style={{
                  backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                }}
                disabled={checkpoint !== 1}
              />
              <button
                type="submit"
                className="absolute right-0 top-0 mt-3 mr-4"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth={1.5}
                  stroke="currentColor"
                  className="w-6 h-6"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                  />
                </svg>
              </button>
            </div>
          </div>
        </div>

        <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={alldata[0]?.strTotalCount}
          reCallApi={handleSubmit}
        />
        <div className="overflow-x-auto relative shadow-md ">
          <>
            {" "}
            <div className="table-wrp block max-h-[30rem] max-w-[19rem] ">
              <table className="w-full text-xs text-left text-clack dark:text-blue-100 font-normal">
                <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                  <th scope="col" className="py-2 px-11 whitespace-nowrap">
                    TXN Id
                  </th>
                  <th scope="col" className="  px-11 py-2 whitespace-nowrap">
                    TXN Status
                  </th>
                  <th scope="col" className="py-2   px-11 whitespace-nowrap">
                    Txn Mode
                  </th>
                  <th scope="col" className="py-2   px-11   whitespace-nowrap">
                    PG TXN Id
                  </th>
                  <th scope="col" className="py-2   px-11 whitespace-nowrap">
                    Amount
                  </th>
                  <th scope="col" className="py-2   px-11 whitespace-nowrap">
                    MSC Amount
                  </th>
                  <th scope="col" className="py-2   px-11 whitespace-nowrap">
                    TXN Type
                  </th>
                  <th scope="col" className="py-2   px-11 whitespace-nowrap">
                    Txn Date
                  </th>
                  <th scope="col" className="py-2   px-11 whitespace-nowrap">
                    Entry Info
                  </th>
                  <th scope="col" className="py-2   px-11 whitespace-nowrap">
                    Entry No
                  </th>
                  <th scope="col" className="py-2   px-11 whitespace-nowrap">
                    CID
                  </th>
                </thead>
                <tbody>
                  {alldata.length > 0 ? (
                    <>
                      {alldata.map(
                        ({
                          transactionId,
                          transactionStatus,
                          transactionMode,
                          pgTxnId,
                          amount,
                          mscAmount,
                          transactionType,
                          transactionDate,
                          entityInfo,
                          entityNumber,
                          cid,
                        }) => (
                          <tr
                            key={uuidv4()}
                            className=" border-b  hover:bg-blue-200 border-blue-400"
                          >
                            <td className="  px-11 py-2 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {transactionId || "-"}
                              </div>
                            </td>
                            <td className="  px-11 py-2 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {transactionStatus || "-"}
                              </div>
                            </td>
                            <td className="  px-11 py-2 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {transactionMode || "-"}
                              </div>
                            </td>
                            <td className="  px-11 py-2 text-right whitespace-nowrap">
                              <div className="text-xs  font-medium text-gray-900">
                                {pgTxnId || "-"}
                              </div>
                            </td>
                            <td className="  px-11 py-2 text-right whitespace-nowrap ">
                              <div className="text-xs  font-medium text-gray-900 ">
                                {amount || "-"}
                              </div>
                            </td>
                            <td className="  px-11 py-2 text-right whitespace-nowrap ">
                              <div className="text-xs  font-medium text-gray-900 ">
                                {mscAmount || "-"}
                              </div>
                            </td>
                            <td className="  px-11 py-2 text-right whitespace-nowrap ">
                              <div className="text-xs  font-medium text-gray-900 ">
                                {transactionType || "-"}
                              </div>
                            </td>
                            <td className="  px-11 py-2 text-right whitespace-nowrap ">
                              <div className="text-xs  font-medium text-gray-900 ">
                                {transactionDate || "-"}
                              </div>
                            </td>
                            <td className="  px-11 py-2 text-right whitespace-nowrap ">
                              <div className="text-xs  font-medium text-gray-900 ">
                                {entityInfo || "-"}
                              </div>
                            </td>

                            <td className="  px-11 py-2 text-right whitespace-nowrap ">
                              <div className="text-xs  font-medium text-gray-900 ">
                                {entityNumber || "-"}
                              </div>
                            </td>
                            <td className="  px-11 py-2 text-right whitespace-nowrap ">
                              <div className="text-xs  font-medium text-gray-900 ">
                                {cid || "-"}
                              </div>
                            </td>
                          </tr>
                        )
                      )}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="12" className=" px-6 py-1 text-center">
                        <span className="text-sm font-normal text-gray-500 ">
                          No data available.
                        </span>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </>
        </div>
      </div>
      {/* <MainPagination itemPageData={setItemPage} tCountData={setTcount} /> */}
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

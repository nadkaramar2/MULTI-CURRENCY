import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import Switch from "@mui/joy/Switch";
import Typography from "@mui/joy/Typography";
import swal from "sweetalert";
import { XMarkIcon } from "@heroicons/react/24/solid";
import {
  ArrowDownTrayIcon,
  MagnifyingGlassIcon,
} from "@heroicons/react/24/outline";
import CustomAlert from "../../layout/CustomAlert";
import MainPagination from "../../layout/MianPagination";
import AutoPagintation from "../../UI/AutoPagintation";

export default function RewardStatements() {
  const [checked, setChecked] = useState(true);
  const [dateOpen, setDateOpen] = useState(false);
  const [duration, setDuration] = useState("");
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [searchQuery, setSearchQuery] = useState("");
  const [checkpoint, setCheckpoint] = useState(0);
  const [itemPage, setItemPage] = useState("10");
  const [tcount, setTcount] = useState("1");

  const [custid, setCustid] = useState("");
  const [accountno, setAccountno] = useState("");
  const [startdate, setStartdate] = useState("");
  const [enddate, setEnddata] = useState("");
  const [alldata, setallData] = useState([]);

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

  // dration

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
      accountno === "" ||
      custid === ""
    ) {
      setError(true);
    } else if (startdate > enddate) {
      swal("please choose lesser date from To Date");
    } else {
      try {
        const response = await amsApi.post(
          // `gl-account-statement/getGLAcountStatementList`,
          `rewardPoints/statement`,
          //   `gl-account-statement/getGLAcountStatementList/pagination/${itemPage}/${tcount}`,
          {
            custId: custid,
            accountNumber: accountno,
            fromDate: startdate,
            toDate: enddate,
            pageSize: itemPage,
            pageNumber: tcount,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code === "S0000") {
          setallData(response.data.transactions);
          // showSuccess(response.data.message);
          setCheckpoint(1);
          setChecked(false);
        } else {
          handleShowError(response.data.message);

          setChecked(true);

          //   handleClick();
          setallData([]);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
        setallData([]);
      }
    }
  };

  const handleClick = () => {
    setStartdate("");
    setEnddata("");
    setAccountno("");
    setCustid("");
    setError("");
    setDuration("");
  };

  // sorted table data

  const [sortColumn, setSortColumn] = useState(""); // State to track the sorted column
  const [sortOrder, setSortOrder] = useState("asc"); // State to track sorting order

  const handleSort = (column) => {
    if (column === sortColumn) {
      // If clicking on the same column, toggle the sorting order
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      // If clicking on a different column, set the new column and default to ascending order
      setSortColumn(column);
      setSortOrder("asc");
    }
  };

  const renderSortArrow = (column) => {
    if (column === sortColumn) {
      return sortOrder === "asc" ? "↑" : "↓";
    }
    return "↕";
  };

  // Sort the data based on the chosen column and order
  const sortedData = [...alldata].sort((a, b) => {
    if (sortOrder === "asc") {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return aValue.localeCompare(bValue);
    } else {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return bValue.localeCompare(aValue);
    }
  });

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Reward Account Statement
              </p>

              <div>
                <button className="inline-flex sm:ml-3 mt-1 sm:mt-0 items-start justify-start px-3.5  focus:outline-none rounded text-xs font-normal">
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
                    <div>
                      <label
                        htmlFor="text"
                        className="block  text-xs font-semibold text-gray-700 dark:text-white"
                      >
                        Cust Id <span className="text-red-600 px-2">*</span>
                      </label>

                      <input
                        type="text"
                        id="account number "
                        value={custid}
                        onChange={(e) => setCustid(e.target.value)}
                        autoComplete="off"
                        placeholder="Customer Id"
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      />
                      {error && custid.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Enter Custid
                        </p>
                      ) : (
                        ""
                      )}
                    </div>

                    <div>
                      <label
                        htmlFor="number"
                        className="block text-xs font-semibold text-gray-700 dark:text-white"
                      >
                        Account Number{""}
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
                        className="block w-full px-3 py-1 text-sm  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                        placeholder="Enter Account Number"
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
                          {error && custid.length <= 0 ? (
                            <p className="text-red-500   text-xs font-medium">
                              Please Enter From date
                            </p>
                          ) : (
                            ""
                          )}
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
                          {error && enddate.length <= 0 ? (
                            <p className="text-red-500   text-xs font-medium">
                              Please Enter To date
                            </p>
                          ) : (
                            ""
                          )}
                        </div>
                      </>
                    )}

                    <div className="">
                      <button
                        title="Click Search Button"
                        type="button"
                        onClick={handleSubmit}
                        data-modal-toggle="defaultModal"
                        className="inline-flex justify-center  bg-blue-500 hover:bg-blue-700 b  focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 border-blue-700 py-1.5 px-5 mx-2 my-5  text-xs font-bold text-white rounded "
                      >
                        <MagnifyingGlassIcon className="h-4 w-3" />
                      </button>

                      <button
                        title="Clear Data"
                        type="button"
                        onClick={handleClick}
                        className="inline-flex justify-center  bg-rose-500 hover:bg-rose-700 b  focus:ring-2 focus:ring-rose-800 focus:ring-offset-2 border-rose-700 py-1.5 px-5 mx-2 my-5  text-xs font-bold text-white rounded "
                      >
                        <XMarkIcon className="h-4 w-3" />
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>
        )}

        <div className="bg-blue-100 px-4 mt-1 flex items-center justify-end  sm:px-3.5">
          <div className=" flex justify-center  rounded-md border border-transparent  px-2 mx-4 text-xs  font-medium text-white  ">
            <div class="pt-2 relative  mx-auto text-gray-600">
              <input
                title="Search Data"
                type="search"
                id="search"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="block w-full px-3 py-1 text-base font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                placeholder="Search.."
                style={{
                  backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                  // You can set other styles here as needed
                }}
                disabled={checkpoint !== 1}
              />

              <button
                type="submit"
                className="absolute right-0 top-0 mt-4 mr-2 px-4  "
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

        {/* table */}

        <div className="bg-white overflow-x-auto relative shadow-md ">
          <div className="table-wrp block max-h-[27rem] ">
            <div>
              <table className="w-full text-xs text-left text-black dark:text-blue-100">
                <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                  <th
                    scope="col"
                    className="py-2.5 px-3.5 whitespace-nowrap"
                    onClick={() => handleSort("transactionDate")}
                  >
                    txn Date {renderSortArrow("transactionDate")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-3.5 whitespace-nowrap"
                    onClick={() => handleSort("transactionType")}
                  >
                    Txn Type {renderSortArrow("transactionType")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-3.5 whitespace-nowrap"
                    onClick={() => handleSort("transactionId")}
                  >
                    Txn Id {renderSortArrow("transactionId")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-3.5 whitespace-nowrap"
                    onClick={() => handleSort("txnMode")}
                  >
                    Txn Mode {renderSortArrow("txnMode")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-3.5 whitespace-nowrap"
                    onClick={() => handleSort("transactionStatus")}
                  >
                    txn Status {renderSortArrow("transactionStatus")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-3.5 whitespace-nowrap"
                    onClick={() => handleSort("points")}
                  >
                    points {renderSortArrow("points")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-3.5 whitespace-nowrap"
                    onClick={() => handleSort("pointstype")}
                  >
                    points type {renderSortArrow("pointstype")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-3.5 whitespace-nowrap"
                    onClick={() => handleSort("pointsdescr")}
                  >
                    points descr {renderSortArrow("pointsdescr")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-3.5 whitespace-nowrap"
                    onClick={() => handleSort("entityInfo")}
                  >
                    entity Info {renderSortArrow("entityInfo")}
                  </th>
                </thead>

                <tbody>
                  {sortedData.length > 0 ? (
                    <>
                      {sortedData
                        .filter(
                          (data) =>
                            data.txnMode
                              .toLowerCase()
                              .includes(searchQuery.toLowerCase()) ||
                            data.transactionStatus
                              .toLowerCase()
                              .includes(searchQuery.toLowerCase()) ||
                            data.transactionId
                              .toLowerCase()
                              .includes(searchQuery.toLowerCase()) ||
                            data.transactionType
                              .toLowerCase()
                              .includes(searchQuery.toLowerCase()) ||
                            data.transactionDate
                              .toLowerCase()
                              .includes(searchQuery.toLowerCase()) ||
                            data.points
                              .toLowerCase()
                              .includes(searchQuery.toLowerCase()) ||
                            data.pointstype
                              .toLowerCase()
                              .includes(searchQuery.toLowerCase()) ||
                            data.pointsdescr
                              .toLowerCase()
                              .includes(searchQuery.toLowerCase()) ||
                            data.entityInfo
                              .toLowerCase()
                              .includes(searchQuery.toLowerCase())
                        )
                        .map((data) => (
                          <tr
                            key={uuidv4()}
                            className="border-b dark:border-neutral-500"
                          >
                            <td className="px-3.5 py-2.5 whitespace-nowrap">
                              <span className="text-xs font-medium text-gray-900">
                                {data.transactionDate || "-"}
                              </span>
                            </td>
                            <td className="px-3.5 py-2.5 whitespace-nowrap">
                              <span className="text-xs font-medium text-gray-900">
                                {data.transactionType || "-"}
                              </span>
                            </td>
                            <td className="px-3.5 py-2.5 whitespace-nowrap">
                              <span className="text-xs font-medium text-gray-900">
                                {data.transactionId || "-"}
                              </span>
                            </td>
                            <td className="whitespace-nowrap px-3.5 py-2.5">
                              <span className="text-xs font-medium text-gray-900">
                                {data.txnMode || "-"}
                              </span>
                            </td>

                            <td className="px-3.5 py-2.5 whitespace-nowrap">
                              <span className="text-xs font-medium text-gray-900">
                                {data.transactionStatus || "-"}
                              </span>
                            </td>

                            <td className="px-3.5 py-2.5  whitespace-nowrap">
                              <span className="text-xs font-medium  text-gray-900">
                                {data.points || "-"}
                              </span>
                            </td>
                            <td className="px-3.5 py-2.5  whitespace-nowrap">
                              <span className="text-xs font-medium  text-gray-900">
                                {data.pointstype || "-"}
                              </span>
                            </td>
                            <td className="px-3.5 py-2.5  whitespace-nowrap">
                              <span className="text-xs font-medium  text-gray-900">
                                {data.pointsdescr || "-"}
                              </span>
                            </td>

                            <td className="px-3.5 py-2.5 whitespace-nowrap">
                              <span className="text-xs font-medium text-gray-900">
                                {data.entityInfo || "-"}
                              </span>
                            </td>
                          </tr>
                        ))}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="8" className="px-3.5 py-1.5 text-center">
                        <span className="text-sm font-normal text-gray-500">
                          No data available.
                        </span>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>
        {/* <MainPagination itemPageData={setItemPage} tCountData={setTcount} /> */}
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

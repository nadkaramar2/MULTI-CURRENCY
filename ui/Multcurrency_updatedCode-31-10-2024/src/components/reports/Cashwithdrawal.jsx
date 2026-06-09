import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import swal from "sweetalert";
import Switch from "@mui/joy/Switch";
import { XMarkIcon } from "@heroicons/react/24/solid";
import Typography from "@mui/joy/Typography";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
import AutoPagintationReport from "./AutoPagintationReport";
export default function Cashwithdrawal() {
  const [list, setList] = useState([]);
  const [accountno, setAccountno] = useState("");
  const [startdate, setStartdate] = useState("");
  const [enddate, setEnddata] = useState("");
    const [searchQuery, setSearchQuery] = useState("");
    const [itemPage, setItemPage] = useState(20);
    const [tcount, setTcount] = useState(1);
    const [error1, setError1] = useState("");
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

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

  //   Pagination

  const [checked, setChecked] = useState(true);
  const [dateOpen, setDateOpen] = useState(false);

  const [duration, setDuration] = useState("");

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
  }, [tcount, itemPage]);
  const handleSubmit = async (event) => {
    // event.preventDefault();

    if (
      startdate === "" ||
      startdate.length === 0 ||
      enddate === "" ||
      enddate.length === 0 ||
      accountno === ""
    ) {
      setError(true);
      //  swal("please enter a valid Input (non-empty values).");
    } else if (startdate > enddate) {
      swal("please choose lesser date from To Date");
    } else {
      try {
        const response = await amsApi.post(
          `denomination_master/getTxnDenominationDetail/pagination/${itemPage}/${tcount}  `,
          {
            strFromDate: startdate,
            strToDate: enddate,
            strTxnType: "WDL",
            toAccount: accountno,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code === "S0000") {
          setList(response.data.denominationMasterResp);
          // showSuccess(response.data.message);
          setChecked(false);
        } else {
          handleShowError(response.data.message);
          setChecked(true);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };
  const handleClick = () => {
    setStartdate("");
    setEnddata("");
    setAccountno("");
    setDuration("");
    setError("");
  };
  useEffect(() => {
    if (searchQuery === "" || searchQuery === " ") {
      handleSubmit();
      setError("");
    }
  }, [searchQuery]);

  const Search = async () => {
    if (startdate === "" || enddate === "") {
      setError1(true);
    } else if (startdate > enddate) {
      swal("Please choose  Lesser Date from To Date");
    } else {
      try {
        const response = await amsApi.post(
          `denomination_master/cashDeptSearch/pagination/10/1`,
          {
            keyword: searchQuery,
            fromDate: startdate,
            toDate: enddate,
          }
        );
        if (response.data.code === "S0000") {
          setList(response.data.denominationMasterResp);
         
          // handleShowSuccess(response.data.message);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        // handleShowError(error.response.data.message);
      }
    }
  };
  
  const [sortColumn, setSortColumn] = useState(""); // State to track the sorted column
  const [sortOrder, setSortOrder] = useState("asc"); // State to track sorting order

  const handleSort = (column) => {
    if (column === sortColumn) {
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
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
  const sortedData = [...list].sort((a, b) => {
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
          <div className="px-4 sm:px-10 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Cash Withdrawal Statement
              </p>
              <div>
                <button className="inline-flex sm:ml-3 mt-1 sm:mt-0 items-start justify-start px-6 focus:outline-none rounded text-xs font-medium">
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
          <div className=" md:col-span-2 lg:col-span-1">
            <form className="">
              <div className="overflow-hidden shadow ">
                <div className=" px-4  sm:p-2 bg-white  ">
                  <div className="grid gap-3  md:grid-cols-5 px-4 ">
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700 dark:text-white"
                      >
                        Agent Account Number{""}
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <input
                        type="text"
                        id="text"
                        value={accountno}
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
                            setError("Please Enter Account number!");
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
                          Please Enter Account number!
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
                    {/* </div>

                  <div className="grid gap-6 mb-2 mt-2 md:grid-cols-3 lg:grid-cols-3 px-8"> */}
                    {dateOpen && (
                      <>
                        <div>
                          <label
                            htmlFor="date"
                            className="block text-xs font-semibold text-gray-700 dark:text-white"
                          >
                            Start Date{" "}
                            <span className="text-red-600 px-2">*</span>
                          </label>
                          <input
                            type="date"
                            id="date"
                            value={startdate}
                            onChange={(e) => setStartdate(e.target.value)}
                            className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                            placeholder=""
                          />
                          {error && startdate.length <= 0 ? (
                            <p className="text-red-500   text-xs font-normal">
                              Please Select Start date
                            </p>
                          ) : (
                            ""
                          )}
                        </div>
                        <div>
                          <label
                            htmlFor="date"
                            className="block text-xs font-semibold text-gray-700 dark:text-white"
                          >
                            End Date{" "}
                            <span className="text-red-600 px-2">*</span>
                          </label>
                          <input
                            type="date"
                            id="text"
                            value={enddate}
                            onChange={(e) => setEnddata(e.target.value)}
                            className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                            placeholder=""
                          />
                          {error && enddate.length <= 0 ? (
                            <p className="text-red-500   text-xs font-medium">
                              Please Select End date
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
                        className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-5 mx-2 my-5 text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                      >
                        <MagnifyingGlassIcon className="h-3 w-3" />
                      </button>
                      <button
                        title="Clear Data"
                        type="button"
                        onClick={handleClick}
                        className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1.5 px-5 mx-2 my-5 text-xs font-medium text-white shadow-sm hover:bg-rose-700 focus:outline-none focus:ring-2 focus:ring-rose-500 focus:ring-offset-2"
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

        <div className="grid gap-2 grid-cols-12 ">
          <div className="col-span-3 flex mt-2 px-2 ">
            <div className="px-4">
              <label
                htmlFor="startdate"
                className="block text-xs px-2 font-semibold text-gray-700 dark:text-white"
              >
                Start Date
              </label>

              <input
                type="date"
                id="startdate"
                value={startdate}
                onChange={(e) => setStartdate(e.target.value)}
                className="block w-32 px-3 py-0.5 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
              />

              {error1 && startdate.length <= 0 ? (
                <p className="text-red-500   text-xs font-sm">
                  Please Select Startdate
                </p>
              ) : (
                ""
              )}
            </div>
            <div>
              <label
                htmlFor="enddate"
                className="block text-xs px-2 font-semibold text-gray-700 dark:text-white"
              >
                End Date
              </label>

              <input
                type="date"
                id="enddate"
                value={enddate}
                onChange={(e) => setEnddata(e.target.value)}
                className="block w-32 px-3 py-0.5 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
              />

              {error1 && enddate.length <= 0 ? (
                <p className="text-red-500   text-xs font-sm">
                  Please Select enddate
                </p>
              ) : (
                ""
              )}
            </div>
          </div>
          <div className="col-span-9 mt-4 px-2">
            <AutoPagintationReport
              addPageperData={setItemPage}
              addCurrentPage={setTcount}
              searchData={setSearchQuery}
              pagePerData={itemPage}
              currentPage={tcount}
              tcount={list[0]?.strTotalCount}
              reCallApi={Search}
            />
          </div>
        </div>
        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={list[0]?.strTotalCount}
          reCallApi={handleSubmit}
        /> */}
        <div className="overflow-x-auto relative shadow-md ">
          <>
            <div className="table-wrp block max-h-[27rem] max-w-[27rem]">
              <table className="w-full text-xs text-left text-black dark:text-blue-100">
                <thead className="text-xm border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                  <th
                    scope="col"
                    className="py-4 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strDate")}
                  >
                    Txn Date{renderSortArrow("strDate")}
                  </th>
                  <th
                    scope="col"
                    className="py-4 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strTime")}
                  >
                    Txn Time{renderSortArrow("strTime")}
                  </th>
                  <th
                    scope="col"
                    className="py-4 px-6 whitespace-nowrap"
                    onClick={() => handleSort("txnId")}
                  >
                    Txn Id{renderSortArrow("txnId")}
                  </th>
                  <th
                    scope="col"
                    className="py-4 px-6 whitespace-nowrap"
                    onClick={() => handleSort("toAccount")}
                  >
                    Agent A/C No{renderSortArrow("toAccount")}
                  </th>
                  <th
                    scope="col"
                    className="py-4 px-6 whitespace-nowrap"
                    onClick={() => handleSort("fromAccount")}
                  >
                    Customer A/C No{renderSortArrow("fromAccount")}
                  </th>
                  <th
                    scope="col"
                    className="py-4 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strTxnType")}
                  >
                    Transaction Type{renderSortArrow("strTxnType")}
                  </th>
                  <th
                    scope="col"
                    className="py-4 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strTxnAmount")}
                  >
                    Amount{renderSortArrow("strTxnAmount")}
                  </th>
                  <th
                    scope="col"
                    className="py-4 px-6 whitespace-nowrap"
                    onClick={() => handleSort("d10")}
                  >
                    Deno 10{renderSortArrow("d10")}
                  </th>
                  <th
                    scope="col"
                    className="py-4 px-6 whitespace-nowrap"
                    onClick={() => handleSort("d20")}
                  >
                    Deno 20{renderSortArrow("d20")}
                  </th>
                  <th
                    scope="col"
                    className="py-4 px-6 whitespace-nowrap"
                    onClick={() => handleSort("d50")}
                  >
                    Deno 50{renderSortArrow("d50")}
                  </th>
                  <th
                    scope="col"
                    className="py-4 px-6 whitespace-nowrap"
                    onClick={() => handleSort("d100")}
                  >
                    Deno 100{renderSortArrow("d100")}
                  </th>
                  <th
                    scope="col"
                    className="py-4 px-6 whitespace-nowrap"
                    onClick={() => handleSort("d200")}
                  >
                    Deno 200{renderSortArrow("d200")}
                  </th>
                  <th
                    scope="col"
                    className="py-4 px-6 whitespace-nowrap"
                    onClick={() => handleSort("d500")}
                  >
                    Deno 500{renderSortArrow("d500")}
                  </th>
                  <th
                    scope="col"
                    className="py-4 px-6 whitespace-nowrap"
                    onClick={() => handleSort("d1000")}
                  >
                    Deno 1000 {renderSortArrow("d1000")}
                  </th>
                  <th
                    scope="col"
                    className="py-4 px-6 whitespace-nowrap"
                    onClick={() => handleSort("d2000")}
                  >
                    Deno 2000{renderSortArrow("d2000")}
                  </th>
                </thead>

                <tbody>
                  {sortedData.length > 0 ? (
                    <>
                      {sortedData.map((data) => (
                        <tr
                          key={uuidv4()}
                          className=" border-b dark:border-neutral-500"
                        >
                          <td className="px-6 py-4 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {data.strDate || "-"}
                            </div>
                          </td>

                          <td className="px-6 py-4 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {data.strTime || "-"}
                            </div>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {data.txnId}
                            </div>
                          </td>

                          <td className="px-6 py-4 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {data.toAccount}
                            </div>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {data.fromAccount}
                            </div>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {data.strTxnType || "-"}
                            </div>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {data.strTxnAmount || "-"}
                            </div>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {data.d10 || "-"}
                            </div>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {""}
                              {data.d20 || "-"}
                            </div>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {""}
                              {data.d50 || "-"}
                            </div>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {""}
                              {data.d100 || "-"}
                            </div>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {""}
                              {data.d200 || "-"}
                            </div>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {""}
                              {data.d500 || "-"}
                            </div>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {""}
                              {data.d1000 || "-"}
                            </div>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {""}
                              {data.d2000 || "-"}
                            </div>
                          </td>
                        </tr>
                      ))}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="15" className="px-6 py-1 text-center">
                        <span className="text-sm font-normal text-gray-500">
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
